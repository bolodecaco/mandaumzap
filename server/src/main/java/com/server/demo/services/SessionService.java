package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.BotRequestDTO;
import com.server.demo.dtos.BotResponseDTO;
import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.ResponseBotConnectionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
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
        session.setActive(updateSessionDTO.isActive());
        Session updatedSession = sessionRepository.save(session);
        return sessionMapper.toDTO(updatedSession);
    }

    public SessionDTO getSessionById(UUID id, String userId) {
        Session session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Sessão de id %s não encontrada", id)));
        return sessionMapper.toDTO(session);
    }

    public ResponseBotConnectionDTO createSession(RequestSessionDTO requestDTO, String userId) {
        Session session = sessionMapper.toEntity(requestDTO);
        session.setUserId(userId);
        Session savedSession = sessionRepository.save(session);
        BotRequestDTO botRequestDTO = sessionMapper.toBotRequestDTO(savedSession);
        try {
            String botResponseJSON = this.requestBotConnection(botRequestDTO).block();
            BotResponseDTO botQrcodeResponse = this.parseQrCode(botResponseJSON);
            return sessionMapper.toResponseBotConnectionDTO(savedSession, botQrcodeResponse);
        } catch (BusinessException e) {
            sessionRepository.delete(savedSession);
            throw new BusinessException("Erro ao criar sessão com o bot");
        }
    }

    private Mono<String> requestBotConnection(BotRequestDTO botRequestDTO) {
        return webClientBuilder
                .baseUrl(botUrl)
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                .path("/api/sessions")
                .queryParam("token", botToken)
                .build())
                .bodyValue(botRequestDTO)
                .retrieve()
                .bodyToMono(String.class);
    }

    private BotResponseDTO parseQrCode(String qrcodeJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(qrcodeJSON, BotResponseDTO.class);
        } catch (JsonProcessingException e) {
            throw new QrCodeParseException("Erro ao processar o QR Code", e);
        }
    }

    public void deleteSession(UUID id, String userId) {
        Session session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Sessão de id %s não encontrada", id)));
        sessionRepository.delete(session);
    }
}
