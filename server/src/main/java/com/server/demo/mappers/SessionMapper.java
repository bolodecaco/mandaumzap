package com.server.demo.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.models.Session;
import com.server.demo.repositories.UserRepository;

import java.util.List;

@Component
public class SessionMapper {

    @Autowired
    private UserRepository userRepository;

    public SessionDTO toDTO(Session session) {
        if (session == null) return null;
        
        SessionDTO dto = new SessionDTO();
        dto.setId(session.getId());
        dto.setUserId(session.getUser().getId());
        dto.setActive(session.isActive());
        return dto;
    }

    public Session toEntity(RequestSessionDTO dto) {
        if (dto == null) return null;

        Session session = new Session();
        session.setActive(dto.isActive());
        session.setUser(userRepository.findById(dto.getOwner().getId())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado")));
        return session;
    }

    public List<SessionDTO> toDTOList(List<Session> sessions) {
        // Implement this method if needed
        return null;
    }
}
