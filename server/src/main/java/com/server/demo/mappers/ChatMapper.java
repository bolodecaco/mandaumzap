package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.models.Chat;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {

    @Mapping(target = "ownerId", source = "chat.owner.id")
    @Mapping(target = "sessionId", source = "chat.session.id")
    ChatDTO toDTO(Chat chat);

    @Mapping(target = "owner.id", source = "ownerId")
    @Mapping(target = "session.id", source = "sessionId")
    Chat toEntity(RequestChatDTO chat);

    List<ChatDTO> toDTOList(List<Chat> chats);
}
