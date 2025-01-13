package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
import com.server.demo.mappers.SessionMapper;
import com.server.demo.models.Session;
import com.server.demo.models.User;
import com.server.demo.repositories.SessionRepository;
import com.server.demo.repositories.UserRepository;

@Service
public class SessionService {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private UserRepository userRepository;

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

    public SessionDTO createSession(RequestSessionDTO requestDTO) {
        Session session = sessionMapper.toEntity(requestDTO);
        Session savedSession = sessionRepository.save(session);
        return sessionMapper.toDTO(savedSession);
    }

    public void deleteSession(UUID id) {
        sessionRepository.deleteById(id);
    }
}
