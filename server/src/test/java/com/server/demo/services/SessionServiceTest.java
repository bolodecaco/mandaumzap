package com.server.demo.services;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.mappers.SessionMapper;
import com.server.demo.models.Session;
import com.server.demo.models.User;
import com.server.demo.repositories.SessionRepository;
import com.server.demo.repositories.UserRepository;

class SessionServiceTest {

    @Mock
    SessionRepository sessionRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    SessionMapper sessionMapper;

    @InjectMocks
    SessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test Session service")
    void createSession() {
        RequestSessionDTO requestSessionDTO = new RequestSessionDTO();
        requestSessionDTO.setActive(true);
        Session session = new Session();
        when(sessionMapper.toEntity(requestSessionDTO)).thenReturn(session);
        sessionService.createSession(requestSessionDTO);
        assertNotNull(session, "Session cannot be null");
    }

    @Test
    @DisplayName("get session by id")
    void getSessionById() {
        UUID id = UUID.randomUUID();
        Session session = new Session();
        session.setId(id);
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId(id);
        when(sessionRepository.findById(id)).thenReturn(java.util.Optional.of(session));
        when(sessionMapper.toDTO(session)).thenReturn(sessionDTO);
        SessionDTO result = sessionService.getSessionById(id);
        assertNotNull(result, "Session cannot be null");
    }

    @Test
    @DisplayName("get users sessions")
    void getUserSessions() {
        UUID userId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(userId);
        Session session = new Session();
        session.setId(UUID.randomUUID());
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId(session.getId());
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        when(sessionRepository.findByUserId(userId)).thenReturn(java.util.List.of(session));
        when(sessionMapper.toDTO(session)).thenReturn(sessionDTO);
        sessionService.getUserSessions(userId);
        assertNotNull(session, "Session cannot be null");
    }

    @Test
    @DisplayName("delete session")
    void deleteSession() {
        UUID id = UUID.randomUUID();
        Session session = new Session();
        when(sessionRepository.findById(id)).thenReturn(java.util.Optional.of(session));
        sessionService.deleteSession(id);
        verify(sessionRepository, times(1)).deleteById(id);
    }

}
