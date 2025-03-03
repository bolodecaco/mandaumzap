package com.server.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.instancio.Instancio;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;

import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
import com.server.demo.exception.BusinessException;
import com.server.demo.mappers.SessionMapper;
import com.server.demo.models.Session;
import com.server.demo.repositories.SessionRepository;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionService sessionService;

    private String userId;

    @Mock
    private WebClient.Builder webClientBuilder;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
        MockitoAnnotations.openMocks(this);
        webClientBuilder = mock(WebClient.Builder.class);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(mock(WebClient.class));
    }

    @Test
    @DisplayName("Buscar sessão por ID com ID válido")
    void getSessionByIdWithValidId() {
        UUID id = UUID.randomUUID();
        Session session = Instancio.create(Session.class);
        SessionDTO responseDTO = Instancio.create(SessionDTO.class);

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(session));
        when(sessionMapper.toDTO(session)).thenReturn(responseDTO);

        SessionDTO data = sessionService.getSessionById(id, userId);

        assertEquals(responseDTO, data);
        verify(sessionRepository).findByIdAndUserId(id, userId);
        verify(sessionMapper).toDTO(session);
    }

    @Test
    @DisplayName("Contar sessões de um usuário")
    void countUserSessions() {
        int expectedCount = 2;

        when(sessionRepository.countByUserId(userId)).thenReturn(expectedCount);

        int result = sessionService.countUserSessions(userId);

        assertEquals(expectedCount, result);
        verify(sessionRepository).countByUserId(userId);
    }

    // @Test
    // @DisplayName("Iniciar sessão com sucesso")
    // void startSessionSuccess() {
    //     UUID id = UUID.randomUUID();
    //     Session session = Instancio.create(Session.class);
    //     RequestBotDTO requestBotDTO = Instancio.create(RequestBotDTO.class);
    //     BotDTO botDTO = Instancio.create(BotDTO.class);
    //     botDTO.setStatus(ConnectionStatusType.open);
    //     String botResponseJSON = "{\"status\":\"open\"}";
    //     when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(session));
    //     when(sessionMapper.toBotRequestDTO(session)).thenReturn(requestBotDTO);
    //     when(sessionService.startBotConnection(requestBotDTO)).thenReturn(Mono.just(botResponseJSON));
    //     when(sessionService.parseQrCode(botResponseJSON)).thenReturn(botDTO);
    //     when(sessionRepository.save(session)).thenReturn(session);
    //     BotConnectionDTO result = sessionService.startSession(id, userId);
    //     assertNotNull(result);
    //     verify(sessionRepository).findByIdAndUserId(id, userId);
    //     verify(sessionMapper).toBotRequestDTO(session);
    //     verify(sessionRepository).save(session);
    // }
    // @Test
    // @DisplayName("Iniciar sessão com status fechado deve falhar")
    // void startSessionWithClosedStatus() {
    //     UUID id = UUID.randomUUID();
    //     Session session = Instancio.create(Session.class);
    //     RequestBotDTO requestBotDTO = Instancio.create(RequestBotDTO.class);
    //     BotDTO botDTO = Instancio.create(BotDTO.class);
    //     botDTO.setStatus(ConnectionStatusType.close);
    //     String botResponseJSON = "{\"status\":\"close\"}";
    //     when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(session));
    //     when(sessionMapper.toBotRequestDTO(session)).thenReturn(requestBotDTO);
    //     when(sessionService.startBotConnection(requestBotDTO)).thenReturn(Mono.just(botResponseJSON));
    //     when(sessionService.parseQrCode(botResponseJSON)).thenReturn(botDTO);
    //     assertThrows(BusinessException.class, () -> sessionService.startSession(id, userId));
    //     verify(sessionRepository).findByIdAndUserId(id, userId);
    //     verify(sessionRepository).delete(session);
    // }
    @Test
    @DisplayName("Buscar sessão por ID com ID inválido deve falhar")
    void getSessionByIdWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> sessionService.getSessionById(id, userId));
        verify(sessionRepository).findByIdAndUserId(id, userId);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    @DisplayName("Listar todas as sessões")
    void getAllSessions() {
        List<Session> sessions = List.of(Instancio.create(Session.class));
        List<SessionDTO> responseDTOs = List.of(Instancio.create(SessionDTO.class));

        when(sessionRepository.findByUserId(userId)).thenReturn(sessions);
        when(sessionMapper.toDTOList(sessions)).thenReturn(responseDTOs);

        List<SessionDTO> result = sessionService.getAllSessions(userId);

        assertEquals(responseDTOs, result);
        verify(sessionRepository).findByUserId(userId);
        verify(sessionMapper).toDTOList(sessions);
    }

    @Test
    @DisplayName("Buscar sessão por ID retorna entidade")
    void getSessionEntityById() {
        UUID id = UUID.randomUUID();
        Session expectedSession = Instancio.create(Session.class);
        SessionDTO expectedDTO = Instancio.create(SessionDTO.class);

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(expectedSession));
        when(sessionMapper.toDTO(expectedSession)).thenReturn(expectedDTO);

        SessionDTO result = sessionService.getSessionById(id, userId);

        assertEquals(expectedDTO, result);
        verify(sessionRepository).findByIdAndUserId(id, userId);
        verify(sessionMapper).toDTO(expectedSession);
    }

    // @Test
    // @DisplayName("Criar sessão com sucesso")
    // void createSessionSuccess() {
    //     Session session = Instancio.create(Session.class);
    //     RequestBotDTO requestBotDTO = Instancio.create(RequestBotDTO.class);
    //     BotDTO botDTO = Instancio.create(BotDTO.class);
    //     String botResponseJSON = "{\"status\":\"open\"}";
    //     when(sessionRepository.countByUserId(userId)).thenReturn(0);
    //     when(sessionRepository.save(any(Session.class))).thenReturn(session);
    //     when(sessionMapper.toBotRequestDTO(session)).thenReturn(requestBotDTO);
    //     when(sessionService.startBotConnection(requestBotDTO)).thenReturn(Mono.just(botResponseJSON));
    //     when(sessionService.parseQrCode(botResponseJSON)).thenReturn(botDTO);
    //     BotConnectionDTO result = sessionService.createSession(userId);
    //     assertNotNull(result);
    //     verify(sessionRepository).countByUserId(userId);
    //     verify(sessionRepository).save(any(Session.class));
    // }
    @Test
    @DisplayName("Criar sessão com limite excedido deve falhar")
    void createSessionWithLimitExceeded() {
        when(sessionRepository.countByUserId(userId)).thenReturn(3);

        assertThrows(BusinessException.class, () -> sessionService.createSession(userId));
        verify(sessionRepository).countByUserId(userId);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
    @DisplayName("Buscar sessão por ID com ID inválido deve falhar ao retornar entidade")
    void getSessionEntityByIdWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> sessionService.getSessionById(id, userId));
        verify(sessionRepository).findByIdAndUserId(id, userId);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    @DisplayName("Atualizar atividade da sessão")
    void updateSessionActivity() {
        UUID id = UUID.randomUUID();
        Session session = Instancio.create(Session.class);
        SessionDTO responseDTO = Instancio.create(SessionDTO.class);
        UpdateSessionDTO updateSessionDTO = Instancio.create(UpdateSessionDTO.class);

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDTO(session)).thenReturn(responseDTO);

        SessionDTO result = sessionService.updateSessionActivity(id, updateSessionDTO, userId);

        assertEquals(responseDTO, result);
        verify(sessionRepository).findByIdAndUserId(id, userId);
        verify(sessionRepository).save(session);
        verify(sessionMapper).toDTO(session);
    }

    @Test
    @DisplayName("Atualizar atividade da sessão com ID inválido deve falhar")
    void updateSessionActivityWithInvalidId() {
        UUID id = UUID.randomUUID();
        UpdateSessionDTO updateSessionDTO = Instancio.create(UpdateSessionDTO.class);

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> sessionService.updateSessionActivity(id, updateSessionDTO, userId));
        verify(sessionRepository).findByIdAndUserId(id, userId);
        verifyNoMoreInteractions(sessionRepository);
        verifyNoInteractions(sessionMapper);
    }

    // @Test
    // @DisplayName("Deletar sessão com sucesso")
    // void deleteSessionSuccess() {
    //     UUID id = UUID.randomUUID();
    //     Session session = Instancio.create(Session.class);
    //     when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(session));
    //     sessionService.deleteSession(id, userId);
    //     verify(sessionRepository).findByIdAndUserId(id, userId);
    //     verify(sessionRepository).delete(session);
    // }
    @Test
    @DisplayName("Deletar sessão com ID inválido deve falhar")
    void deleteSessionWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> sessionService.deleteSession(id, userId));
        verify(sessionRepository).findByIdAndUserId(id, userId);
        verifyNoMoreInteractions(sessionRepository);
    }

    // @Test
    // @DisplayName("Fechar sessão com sucesso")
    // void closeSessionSuccess() {
    //     UUID id = UUID.randomUUID();
    //     Session session = Instancio.create(Session.class);
    //     when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(session));
    //     sessionService.closeSession(id, userId);
    //     verify(sessionRepository).findByIdAndUserId(id, userId);
    //     verify(sessionRepository).save(session);
    // }
    // @Test
    // @DisplayName("Fechar sessão com ID inválido deve falhar")
    // void closeSessionWithInvalidId() {
    //     UUID id = UUID.randomUUID();
    //     when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());
    //     assertThrows(BusinessException.class, () -> sessionService.closeSession(id, userId));
    //     verify(sessionRepository).findByIdAndUserId(id, userId);
    //     verifyNoMoreInteractions(sessionRepository);
    // }
    // @Test
    // @DisplayName("Deletar todas as sessões de um usuário")
    // void deleteAllSessions() {
    //     sessionService.deleteAllSessions(userId);
    //     verify(sessionRepository).deleteAllByUserId(userId);
    // }
    // @Test
    // @DisplayName("Parsear QR Code com sucesso")
    // void parseQrCodeSuccess() {
    //     String qrCodeJSON = "{\"status\":\"open\"}";
    //     BotDTO expectedBotDTO = Instancio.create(BotDTO.class);
    //     BotDTO result = sessionService.parseQrCode(qrCodeJSON);
    //     assertNotNull(result);
    //     assertEquals(expectedBotDTO.getStatus(), result.getStatus());
    // }
    // @Test
    // @DisplayName("Parsear QR Code inválido deve falhar")
    // void parseQrCodeWithInvalidJSON() {
    //     String invalidQrCodeJSON = "invalid JSON";
    //     assertThrows(QrCodeParseException.class, () -> sessionService.parseQrCode(invalidQrCodeJSON));
    // }
}
