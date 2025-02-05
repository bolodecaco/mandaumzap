package com.server.demo.mappers;

import org.mapstruct.*;

import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.models.Session;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper {

    SessionDTO toDTO(Session session);

    Session toEntity(RequestSessionDTO session);

    List<SessionDTO> toDTOList(List<Session> sessions);
}
