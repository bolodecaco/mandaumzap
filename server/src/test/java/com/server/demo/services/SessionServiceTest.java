package com.server.demo.services;

import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
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

    private AutoCloseable closeable;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void destroy() throws Exception {
        closeable.close();
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
    @DisplayName("Deletar sessão com ID válido")
    void deleteSessionWithValidId() {
        UUID id = UUID.randomUUID();

        sessionService.deleteSession(id);

        verify(sessionRepository).deleteById(id);
    }
}
