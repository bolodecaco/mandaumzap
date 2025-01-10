package com.server.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.mappers.SessionMapper;
import com.server.demo.models.Session;
import com.server.demo.repositories.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionRepository sessionRepository;

    public List<SessionDTO> getAllSessions() {
        List<Session> sessions = sessionRepository.findAll();
        return sessions.stream()
                .map(sessionMapper::toDTO)
                .toList();
    }

    public Optional<SessionDTO> getSessionById(UUID id) {
        return sessionRepository.findById(id)
                .map(sessionMapper::toDTO);
    }

    public SessionDTO createSession(RequestSessionDTO requestDTO) {
        Session session = sessionMapper.toEntity(requestDTO);
        Session savedSession = sessionRepository.save(session);
        return sessionMapper.toDTO(savedSession);
    }

    public SessionDTO updateSession(RequestSessionDTO sessionDetails) {
        Session session = sessionRepository.findById(sessionDetails.getOwner()
                .getId())
                .orElseThrow(() -> new RuntimeException("Sessão não encontrada"));
        
        session.setActive(sessionDetails.isActive());
        
        Session updatedSession = sessionRepository.save(session);
        return sessionMapper.toDTO(updatedSession);
    }

    public void deleteSession(UUID id) {
        sessionRepository.deleteById(id);
    }
}
