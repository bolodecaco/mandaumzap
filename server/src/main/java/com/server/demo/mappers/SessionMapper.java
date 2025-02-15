package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.BotRequestDTO;
import com.server.demo.dtos.BotResponseDTO;
import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.ResponseBotConnectionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.models.Session;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper {

    SessionDTO toDTO(Session session);

    Session toEntity(RequestSessionDTO session);

    List<SessionDTO> toDTOList(List<Session> sessions);

    @Mapping(target = "sessionId", source = "id")
    BotRequestDTO toBotRequestDTO(Session requestSessionDTO);

    @Mapping(target = "id", source = "session.id")
    ResponseBotConnectionDTO toResponseBotConnectionDTO(Session session, BotResponseDTO response);

}
