package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.dtos.BotQrCodeResponseDTO;
import com.server.demo.dtos.BotRequestDTO;
import com.server.demo.dtos.QrCodeResponseDTO;
import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
import com.server.demo.exception.QrCodeParseException;
import com.server.demo.mappers.SessionMapper;
import com.server.demo.models.Session;
import com.server.demo.models.User;
import com.server.demo.repositories.SessionRepository;
import com.server.demo.repositories.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class SessionService {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<SessionDTO> getAllSessions() {
        List<Session> sessions = sessionRepository.findAll();
        return sessionMapper.toDTOList(sessions);
    }

    public SessionDTO updateSessionActivity(UUID id, UpdateSessionDTO updateSessionDTO) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Sessão de id %s não encontrada", id)));
        session.setActive(updateSessionDTO.isActive());
        Session updatedSession = sessionRepository.save(session);
        return sessionMapper.toDTO(updatedSession);
    }

    public List<SessionDTO> getUserSessions(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(String.format("Usuário de id %s não encontrado", userId)));
        List<Session> sessions = sessionRepository.findByUser(user);
        return sessionMapper.toDTOList(sessions);
    }

    public SessionDTO getSessionById(UUID id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Sessão de id %s não encontrada", id)));
        return sessionMapper.toDTO(session);
    }

    public BotQrCodeResponseDTO createSession(RequestSessionDTO requestDTO) {
        Session session = sessionMapper.toEntity(requestDTO);
        Session savedSession = sessionRepository.save(session);
        SessionDTO sessionDTO = sessionMapper.toDTO(savedSession);
        BotRequestDTO botRequestDTO = sessionMapper.toBotRequestDTO(sessionDTO);
        String qrcodeJSON = this.requestBotConnection(botRequestDTO).block();
        QrCodeResponseDTO botQrcodeResponse = this.parseQrCode(qrcodeJSON);
        return sessionMapper.toBotQrCodeResponseDTO(sessionDTO, botQrcodeResponse);
    }

    private Mono<String> requestBotConnection(BotRequestDTO botRequestDTO) {
        String token = "token";
        return webClientBuilder
                .baseUrl("http://localhost:7000")
                .build()
                .post()
                .uri(uriBuilder -> uriBuilder
                .path("/api/sessions")
                .queryParam("token", token)
                .build())
                .bodyValue(botRequestDTO)
                .retrieve()
                .bodyToMono(String.class);
    }

    private QrCodeResponseDTO parseQrCode(String qrcodeJSON) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(qrcodeJSON, QrCodeResponseDTO.class);
        } catch (JsonProcessingException e) {
            throw new QrCodeParseException("Erro ao processar o QR Code", e);
        }
    }

    public void deleteSession(UUID id) {
        sessionRepository.deleteById(id);
    }
}
