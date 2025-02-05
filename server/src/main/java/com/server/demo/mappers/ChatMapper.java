package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
import com.server.demo.models.Chat;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {

    @Mapping(target = "sessionId", source = "chat.session.id")
    ChatDTO toDTO(Chat chat);

    @Mapping(target = "session.id", source = "sessionId")
    Chat toEntity(RequestChatDTO chat);

    List<ChatDTO> toDTOList(List<Chat> chats);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    Chat updateEntityFromDTO(UpdateChatDTO dto, @MappingTarget Chat chat);

}
