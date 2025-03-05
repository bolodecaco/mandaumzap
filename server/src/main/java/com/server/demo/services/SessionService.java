package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.BotConnectionDTO;
import com.server.demo.dtos.BotDTO;
import com.server.demo.dtos.RequestBotDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
import com.server.demo.enums.ConnectionStatusType;
import com.server.demo.exception.BusinessException;
import com.server.demo.exception.QrCodeParseException;
import com.server.demo.mappers.SessionMapper;
import com.server.demo.models.Session;
import com.server.demo.repositories.SessionRepository;

import reactor.core.publisher.Mono;

@Service
public class SessionService {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${bot.whatsapp.url}")
    private String botUrl;

    @Value("${bot.whatsapp.token}")
    private String botToken;

    public List<SessionDTO> getAllSessions(String userId) {
        List<Session> sessions = sessionRepository.findByUserId(userId);
        return sessionMapper.toDTOList(sessions);
    }

    public SessionDTO updateSessionActivity(UUID id, UpdateSessionDTO updateSessionDTO, String userId) {
        Session session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Sessão de id %s não encontrada", id)));
        session.setStatus(updateSessionDTO.getStatus());
        Session updatedSession = sessionRepository.save(session);
        return sessionMapper.toDTO(updatedSession);
    }

    public SessionDTO getSessionById(UUID id, String userId) {
        Session session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Sessão de id %s não encontrada", id)));
        return sessionMapper.toDTO(session);
    }

    public int countUserSessions(String userId) {
        return sessionRepository.countByUserId(userId);
    }

    public BotConnectionDTO startSession(UUID id, String userId) {
        Session session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Sessão de id %s não encontrada", id)));
        RequestBotDTO requestBotDTO = sessionMapper.toBotRequestDTO(session);
        try {
            String botResponseJSON = this.startBotConnection(requestBotDTO).block();
            BotDTO botDTO = this.parseQrCode(botResponseJSON);
            ConnectionStatusType status = botDTO.getStatus();
            if (status == ConnectionStatusType.close || status == ConnectionStatusType.error) {
                sessionRepository.delete(session);
                throw new BusinessException("A sessão que você quer se conectar está fechada");
            }
            session.setStatus(status);
            sessionRepository.save(session);
            return sessionMapper.toResponseBotConnectionDTO(session, botDTO);
        } catch (BusinessException e) {
            throw new BusinessException("Erro ao iniciar sessão com o bot");
        }
    }

    public BotConnectionDTO createSession(String userId) {
        if (countUserSessions(userId) >= 3) {
            throw new BusinessException("Usuário já possui o limite de sessões");
        }
        Session session = new Session();
        session.setUserId(userId);
        session.setStatus(ConnectionStatusType.pending);
        Session savedSession = sessionRepository.save(session);
        RequestBotDTO requestBotDTO = sessionMapper.toBotRequestDTO(savedSession);
        try {
            String botResponseJSON = this.startBotConnection(requestBotDTO).block();
            BotDTO botDTO = this.parseQrCode(botResponseJSON);
            return sessionMapper.toResponseBotConnectionDTO(savedSession, botDTO);
        } catch (BusinessException e) {
            throw new BusinessException("Erro ao criar sessão com o bot");
        }
    }

    public Mono<String> startBotConnection(RequestBotDTO botRequestDTO) {
        return webClientBuilder
                .baseUrl(botUrl)
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                .path("/api/sessions")
                .queryParam("token", botToken)
                .build())
                .bodyValue(botRequestDTO)
                .exchangeToMono(response -> {
                    int statusCode = response.statusCode().value();
                    return response.bodyToMono(String.class)
                            .map(body -> {
                                if (statusCode >= 400) {
                                    throw new BusinessException(
                                            "Erro ao iniciar sessão: " + statusCode + " - " + body);
                                }
                                return body;
                            });
                });
    }

    public void requestDeleteSession(UUID sessionId, String userId) {
        webClientBuilder
                .baseUrl(botUrl)
                .build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                .path("/api/sessions/{userId}/{sessionId}")
                .queryParam("token", botToken)
                .build(userId, sessionId.toString()))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void requestDeleteAllSession(String userId) {
        webClientBuilder
                .baseUrl(botUrl)
                .build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                .path("/api/sessions/{userId}/")
                .queryParam("token", botToken)
                .build(userId))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void requestCloseSession(UUID sessionId, String userId) {
        webClientBuilder
                .baseUrl(botUrl)
                .build()
                .delete()
                .uri(uriBuilder -> uriBuilder
                .path("/api/sessions/close/{userId}/{sessionId}")
                .queryParam("token", botToken)
                .build(userId, sessionId.toString()))
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public BotDTO parseQrCode(String qrcodeJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(qrcodeJSON, BotDTO.class);
        } catch (JsonProcessingException e) {
            throw new QrCodeParseException("Erro ao processar o QR Code", e);
        }
    }

    public void deleteSession(UUID id, String userId) {
        Session session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Sessão de id %s não encontrada", id)));
        requestDeleteSession(session.getId(), session.getUserId());
        sessionRepository.delete(session);
    }

    public void closeSession(UUID id, String userId) {
        Session session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Sessão de id %s não encontrada", id)));
        requestCloseSession(session.getId(), session.getUserId());
        session.setStatus(ConnectionStatusType.close);
        sessionRepository.save(session);
    }

    @Transactional
    public void deleteAllSessions(String userId) {
        requestDeleteAllSession(userId);
        sessionRepository.deleteAllByUserId(userId);
    }
}
