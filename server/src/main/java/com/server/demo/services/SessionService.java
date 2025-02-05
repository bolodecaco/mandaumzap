package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
import com.server.demo.exception.BusinessException;
import com.server.demo.mappers.SessionMapper;
import com.server.demo.models.Session;
import com.server.demo.repositories.SessionRepository;

@Service
public class SessionService {

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private SessionRepository sessionRepository;

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

    public SessionDTO createSession(RequestSessionDTO requestDTO, String userId) {
        Session session = sessionMapper.toEntity(requestDTO);
        session.setUserId(userId);
        Session savedSession = sessionRepository.save(session);
        return sessionMapper.toDTO(savedSession);
    }

    public void deleteSession(UUID id, String userId) {
        Session session = sessionRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new BusinessException(String.format("Sessão de id %s não encontrada", id)));
        sessionRepository.delete(session);
    }
}
