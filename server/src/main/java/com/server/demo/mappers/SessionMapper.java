package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.BotConnectionDTO;
import com.server.demo.dtos.BotDTO;
import com.server.demo.dtos.RequestBotDTO;
import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.models.Session;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper {

    SessionDTO toDTO(Session session);

    Session toEntity(RequestSessionDTO session);

    List<SessionDTO> toDTOList(List<Session> sessions);

    @Mapping(target = "sessionId", source = "id")
    RequestBotDTO toBotRequestDTO(Session requestSessionDTO);

    @Mapping(target = "id", source = "session.id")
    BotConnectionDTO toResponseBotConnectionDTO(Session session, BotDTO response);
}
