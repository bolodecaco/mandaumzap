package com.server.demo.services;

import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
import com.server.demo.mappers.SessionMapper;
import com.server.demo.models.Session;
import com.server.demo.models.User;
import com.server.demo.repositories.SessionRepository;
import com.server.demo.repositories.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SessionMapper sessionMapper;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("Criar sessão com dados válidos")
    void createSessionWithValidData() {
        RequestSessionDTO requestDTO = Instancio.create(RequestSessionDTO.class);
        Session session = Instancio.create(Session.class);
        SessionDTO responseDTO = Instancio.create(SessionDTO.class);

        when(sessionMapper.toEntity(requestDTO)).thenReturn(session);
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDTO(session)).thenReturn(responseDTO);

        SessionDTO data = sessionService.createSession(requestDTO);

        assertEquals(responseDTO, data);
        verify(sessionMapper).toEntity(requestDTO);
        verify(sessionRepository).save(session);
        verify(sessionMapper).toDTO(session);
    }

    @Test
    @DisplayName("Buscar sessão por ID com ID válido")
    void getSessionByIdWithValidId() {
        UUID id = UUID.randomUUID();
        Session session = Instancio.create(Session.class);
        SessionDTO responseDTO = Instancio.create(SessionDTO.class);

        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
        when(sessionMapper.toDTO(session)).thenReturn(responseDTO);

        SessionDTO data = sessionService.getSessionById(id);

        assertEquals(responseDTO, data);
        verify(sessionRepository).findById(id);
        verify(sessionMapper).toDTO(session);
    }

    @Test
    @DisplayName("Buscar sessão por ID com ID inválido deve falhar")
    void getSessionByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        
        when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionService.getSessionById(id));
        verify(sessionRepository).findById(id);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    @DisplayName("Buscar sessões de usuário")
    void getUsersSessions() {
        UUID userId = UUID.randomUUID();
        User user = Instancio.create(User.class);
        List<Session> sessions = List.of(Instancio.create(Session.class));
        List<SessionDTO> responseDTOs = List.of(Instancio.create(SessionDTO.class));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(sessionRepository.findByUser(user)).thenReturn(sessions);
        when(sessionMapper.toDTOList(sessions)).thenReturn(responseDTOs);

        List<SessionDTO> data = sessionService.getUserSessions(userId);

        assertEquals(responseDTOs, data);
        verify(userRepository).findById(userId);
        verify(sessionRepository).findByUser(user);
        verify(sessionMapper).toDTOList(sessions);
    }

    @Test
    @DisplayName("Buscar sessões de usuário com ID inválido deve falhar")
    void getUsersSessionsWithInvalidUserId() {
        UUID userId = UUID.randomUUID();
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionService.getUserSessions(userId));
        verify(userRepository).findById(userId);
        verifyNoInteractions(sessionRepository);
        verifyNoInteractions(sessionMapper);
    }

    @Test
    @DisplayName("Listar todas as sessões")
    void getAllSessions() {
        List<Session> sessions = List.of(Instancio.create(Session.class));
        List<SessionDTO> responseDTOs = List.of(Instancio.create(SessionDTO.class));

        when(sessionRepository.findAll()).thenReturn(sessions);
        when(sessionMapper.toDTOList(sessions)).thenReturn(responseDTOs);

        List<SessionDTO> result = sessionService.getAllSessions();

        assertEquals(responseDTOs, result);
        verify(sessionRepository).findAll();
        verify(sessionMapper).toDTOList(sessions);
    }

    @Test
    @DisplayName("Buscar sessão por ID retorna entidade")
    void getSessionEntityById() {
        UUID id = UUID.randomUUID();
        Session expectedSession = Instancio.create(Session.class);
        SessionDTO expectedDTO = Instancio.create(SessionDTO.class);

        when(sessionRepository.findById(id)).thenReturn(Optional.of(expectedSession));
        when(sessionMapper.toDTO(expectedSession)).thenReturn(expectedDTO);

        SessionDTO result = sessionService.getSessionById(id);

        assertEquals(expectedDTO, result);
        verify(sessionRepository).findById(id);
        verify(sessionMapper).toDTO(expectedSession);
    }

    @Test
    @DisplayName("Buscar sessão por ID com ID inválido deve falhar ao retornar entidade")
    void getSessionEntityByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        
        when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionService.getSessionById(id));
        verify(sessionRepository).findById(id);
    }

    @Test
    @DisplayName("Deletar sessão com ID válido")
    void deleteSessionWithValidId() {
        UUID id = UUID.randomUUID();

        sessionService.deleteSession(id);

        verify(sessionRepository).deleteById(id);
    }

    @Test
    @DisplayName("Atualizar atividade da sessão")
    void updateSessionActivity() {
        UUID id = UUID.randomUUID();
        Session session = Instancio.create(Session.class);
        SessionDTO responseDTO = Instancio.create(SessionDTO.class);
        UpdateSessionDTO updateSessionDTO = Instancio.create(UpdateSessionDTO.class);
        
        when(sessionRepository.findById(id)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);
        when(sessionMapper.toDTO(session)).thenReturn(responseDTO);

        SessionDTO result = sessionService.updateSessionActivity(id, updateSessionDTO);

        assertEquals(responseDTO, result);
        verify(sessionRepository).findById(id);
        verify(sessionRepository).save(session);
        verify(sessionMapper).toDTO(session);
    }

    @Test
    @DisplayName("Atualizar atividade da sessão com ID inválido deve falhar")
    void updateSessionActivityWithInvalidId() {
        UUID id = UUID.randomUUID();
        UpdateSessionDTO updateSessionDTO = Instancio.create(UpdateSessionDTO.class);
        
        when(sessionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionService.updateSessionActivity(id, updateSessionDTO));
        verify(sessionRepository).findById(id);
        verifyNoMoreInteractions(sessionRepository);
        verifyNoInteractions(sessionMapper);
    }
}