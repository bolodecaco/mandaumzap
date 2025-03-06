package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.models.Message;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {

    @Mapping(target = "sessionId", source = "session.id")
    @Mapping(target = "broadcastListId", source = "broadcastList.id")
    @Mapping(target = "lastSent", source = "lastSentAt")
    MessageDTO toDTO(Message message);

    @Mapping(target = "session.id", source = "sessionId")
    @Mapping(target = "broadcastList.id", source = "broadcastListId")
    Message toEntity(RequestMessageDTO messageDTO);

    RequestMessageDTO toRequestDTO(MessageDTO messageDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    List<MessageDTO> toDTOList(List<Message> messages);
}
