package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.BotQrCodeResponseDTO;
import com.server.demo.dtos.BotRequestDTO;
import com.server.demo.dtos.QrCodeResponseDTO;
import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.models.Session;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionMapper {

    @Mapping(target = "userId", source = "session.user.id")
    SessionDTO toDTO(Session session);

    @Mapping(target = "user.id", source = "userId")
    Session toEntity(RequestSessionDTO session);

    List<SessionDTO> toDTOList(List<Session> sessions);

    @Mapping(target = "sessionId", source = "session.id")
    BotRequestDTO toBotRequestDTO(SessionDTO session);

    @Mapping(target = "qrcode", source = "qrCode.qrcode")
    BotQrCodeResponseDTO toBotQrCodeResponseDTO(SessionDTO session, QrCodeResponseDTO qrCode);
}
