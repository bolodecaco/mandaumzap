package com.server.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.instancio.Instancio;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Mock
    private WebClient webClient;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
        MockitoAnnotations.openMocks(this);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        sessionService = spy(new SessionService());
        ReflectionTestUtils.setField(sessionService, "sessionRepository", sessionRepository);
        ReflectionTestUtils.setField(sessionService, "sessionMapper", sessionMapper);
        ReflectionTestUtils.setField(sessionService, "webClientBuilder", webClientBuilder);

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

    
    @Test
    @DisplayName("Criar sessão com limite excedido deve falhar")
    void createSessionWithLimitExceeded() {
        when(sessionRepository.countByUserId(userId)).thenReturn(3);

        assertThrows(BusinessException.class, () -> sessionService.createSession(userId));
        verify(sessionRepository).countByUserId(userId);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
    @DisplayName("Criar sessão deve atualizar o status da sessão e o userId corretamente")
    void createSessionShouldSetUserIdAndStatusCorrectly() {
        when(sessionRepository.countByUserId(userId)).thenReturn(2);
        
        BotConnectionDTO expectedResult = new BotConnectionDTO();
        String botResponseJSON = "{\"status\":\"open\"}";
        BotDTO botDTO = new BotDTO();
        botDTO.setStatus(ConnectionStatusType.open);
        
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        
        when(sessionRepository.save(sessionCaptor.capture())).thenReturn(new Session());
        when(sessionMapper.toBotRequestDTO(any(Session.class))).thenReturn(new RequestBotDTO());
        doReturn(Mono.just(botResponseJSON)).when(sessionService).startBotConnection(any(RequestBotDTO.class));
        doReturn(botDTO).when(sessionService).parseQrCode(botResponseJSON);
        when(sessionMapper.toResponseBotConnectionDTO(any(Session.class), any(BotDTO.class)))
            .thenReturn(expectedResult);
        
        // Act
        BotConnectionDTO result = sessionService.createSession(userId);
        
        // Assert
        assertNotNull(result);
        
        // Verify the session was created with correct values
        Session capturedSession = sessionCaptor.getValue();
        assertEquals(userId, capturedSession.getUserId());
        assertEquals(ConnectionStatusType.pending, capturedSession.getStatus());
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
    @DisplayName("Atualizar sessão válida deve retornar sucesso")
    void updateSessionStatusShouldCorrectlyUpdateStatus() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        UpdateSessionDTO updateDTO = new UpdateSessionDTO();
        updateDTO.setStatus(ConnectionStatusType.open);
        
        Session existingSession = new Session();
        existingSession.setId(sessionId);
        existingSession.setStatus(ConnectionStatusType.pending);
        
        Session updatedSession = new Session();
        updatedSession.setId(sessionId);
        updatedSession.setStatus(ConnectionStatusType.open);
        
        SessionDTO expectedDTO = new SessionDTO();
        expectedDTO.setStatus(ConnectionStatusType.open);
        
        when(sessionRepository.findByIdAndUserId(sessionId, userId))
            .thenReturn(Optional.of(existingSession));
        when(sessionRepository.save(any(Session.class))).thenReturn(updatedSession);
        when(sessionMapper.toDTO(updatedSession)).thenReturn(expectedDTO);
        
        // Act
        SessionDTO result = sessionService.updateSessionActivity(sessionId, updateDTO, userId);
        
        // Assert
        assertEquals(ConnectionStatusType.open, result.getStatus());
        verify(sessionRepository).findByIdAndUserId(sessionId, userId);
        
        // Capture the session that was passed to save to verify status was set
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(sessionCaptor.capture());
        assertEquals(ConnectionStatusType.open, sessionCaptor.getValue().getStatus());
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

    @Test
    @DisplayName("Deletar sessão com ID inválido deve falhar")
    void deleteSessionWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> sessionService.deleteSession(id, userId));
        verify(sessionRepository).findByIdAndUserId(id, userId);
        verifyNoMoreInteractions(sessionRepository);
    }

    @Test
    @DisplayName("Iniciar sessão deve retornar a conexão com o bot corretamente")
    void startSessionShouldReturnBotConnection() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        Session session = new Session();
        session.setId(sessionId);
        session.setUserId(userId);
        session.setStatus(ConnectionStatusType.pending);
        
        RequestBotDTO requestBotDTO = new RequestBotDTO();
        String botResponseJSON = "{\"status\":\"open\"}";
        BotDTO botDTO = new BotDTO();
        botDTO.setStatus(ConnectionStatusType.open);
        BotConnectionDTO expectedResult = new BotConnectionDTO();
        
        when(sessionRepository.findByIdAndUserId(sessionId, userId))
            .thenReturn(Optional.of(session));
        when(sessionMapper.toBotRequestDTO(session)).thenReturn(requestBotDTO);
        doReturn(Mono.just(botResponseJSON)).when(sessionService).startBotConnection(requestBotDTO);
        doReturn(botDTO).when(sessionService).parseQrCode(botResponseJSON);
        when(sessionMapper.toResponseBotConnectionDTO(any(Session.class), any(BotDTO.class)))
            .thenReturn(expectedResult);
        
        // Act
        BotConnectionDTO result = sessionService.startSession(sessionId, userId);
        
        // Assert
        assertNotNull(result);
        
        // Verify session status was updated
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(sessionCaptor.capture());
        assertEquals(ConnectionStatusType.open, sessionCaptor.getValue().getStatus());
    }

    @Test
    @DisplayName("Iniciar sessão fechada deve falhar")
    void startSessionWithClosedStatusShouldThrowException() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        Session session = new Session();
        session.setId(sessionId);
        session.setUserId(userId);
        
        RequestBotDTO requestBotDTO = new RequestBotDTO();
        String botResponseJSON = "{\"status\":\"close\"}";
        BotDTO botDTO = new BotDTO();
        botDTO.setStatus(ConnectionStatusType.close);
        
        when(sessionRepository.findByIdAndUserId(sessionId, userId))
            .thenReturn(Optional.of(session));
        when(sessionMapper.toBotRequestDTO(session)).thenReturn(requestBotDTO);
        doReturn(Mono.just(botResponseJSON)).when(sessionService).startBotConnection(requestBotDTO);
        doReturn(botDTO).when(sessionService).parseQrCode(botResponseJSON);
        
        // Act & Assert
        BusinessException exception = assertThrows(BusinessException.class, 
            () -> sessionService.startSession(sessionId, userId));
        
        assertEquals("Erro ao iniciar sessão com o bot", exception.getMessage());
        verify(sessionRepository).delete(session);
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Iniciar a conexão com o bot deve retornar um JSON")
    void startBotConnectionShouldReturnResponseJSON() {
        // Arrange
        RequestBotDTO requestBotDTO = new RequestBotDTO();
        String expectedResponse = "{\"status\":\"open\"}";
        
        // Use doReturn to stub the method call directly on the spied sessionService
        doReturn(Mono.just(expectedResponse)).when(sessionService).startBotConnection(requestBotDTO);
        
        // Act
        String result = sessionService.startBotConnection(requestBotDTO).block();
        
        // Assert
        assertEquals(expectedResponse, result);
    }

    @Test
    @DisplayName("Formatar para QR Code deve trazer uma resposta do Bot")
    void parseQrCodeShouldReturnBotDTO() {
        // Arrange
        String qrCodeJSON = "{\"status\":\"open\",\"qrcode\":\"data:image/png;base64,example\"}";
        
        // Act
        BotDTO result = sessionService.parseQrCode(qrCodeJSON);
        
        // Assert
        assertNotNull(result);
        assertEquals(ConnectionStatusType.open, result.getStatus());
    }

    @Test
    @DisplayName("Formatar um QR Code inválido deve falhar")
    void parseInvalidQrCodeShouldThrowException() {
        // Arrange
        String invalidJSON = "invalid json";
        
        // Act & Assert
        assertThrows(QrCodeParseException.class, () -> sessionService.parseQrCode(invalidJSON));
    }

    @Test
    @DisplayName("Deve obter sucesso ao deletar uma sessão válida")
    void deleteSessionShouldCallRepositoryDelete() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        Session session = new Session();
        session.setId(sessionId);
        session.setUserId(userId);
        
        when(sessionRepository.findByIdAndUserId(sessionId, userId))
            .thenReturn(Optional.of(session));
        doNothing().when(sessionService).requestDeleteSession(sessionId, userId);
        
        // Act
        sessionService.deleteSession(sessionId, userId);
        
        // Assert
        verify(sessionRepository).delete(session);
        verify(sessionService).requestDeleteSession(sessionId, userId);
    }

    @Test
    @DisplayName("Deve atualizar o status da sessão ao fechá-la")
    void closeSessionShouldUpdateStatus() {
        // Arrange
        UUID sessionId = UUID.randomUUID();
        Session session = new Session();
        session.setId(sessionId);
        session.setUserId(userId);
        session.setStatus(ConnectionStatusType.open);
        
        when(sessionRepository.findByIdAndUserId(sessionId, userId))
            .thenReturn(Optional.of(session));
        doNothing().when(sessionService).requestCloseSession(sessionId, userId);
        
        // Act
        sessionService.closeSession(sessionId, userId);
        
        // Assert
        ArgumentCaptor<Session> sessionCaptor = ArgumentCaptor.forClass(Session.class);
        verify(sessionRepository).save(sessionCaptor.capture());
        assertEquals(ConnectionStatusType.close, sessionCaptor.getValue().getStatus());
        verify(sessionService).requestCloseSession(sessionId, userId);
    }

    @Test
    @DisplayName("Deletar todas as sessões deve chamar o repositório corretamente")
    void deleteAllSessionsShouldCallRepositoryDeleteAll() {
        // Arrange
        doNothing().when(sessionService).requestDeleteAllSession(userId);
        
        // Act
        sessionService.deleteAllSessions(userId);
        
        // Assert
        verify(sessionRepository).deleteAllByUserId(userId);
        verify(sessionService).requestDeleteAllSession(userId);
    }
}
