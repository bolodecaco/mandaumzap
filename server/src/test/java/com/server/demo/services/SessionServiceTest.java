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
        userId = "67128981-aoiadjadjada";
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
    @DisplayName("Buscar sessão por ID com ID inválido deve falhar")
    void getSessionByIdWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionService.getSessionById(id, userId));
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
    @DisplayName("Buscar sessão por ID com ID inválido deve falhar ao retornar entidade")
    void getSessionEntityByIdWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(sessionRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionService.getSessionById(id, userId));
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

        assertThrows(RuntimeException.class, () -> sessionService.updateSessionActivity(id, updateSessionDTO, userId));
        verify(sessionRepository).findByIdAndUserId(id, userId);
        verifyNoMoreInteractions(sessionRepository);
        verifyNoInteractions(sessionMapper);
    }
}
