package com.server.demo.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.models.Message;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MessageMapper {

    MessageDTO toDTO(Message message);

    Message toEntity(RequestMessageDTO messageDTO);

    RequestMessageDTO toRequestDTO(MessageDTO messageDTO);

    List<MessageDTO> toDTOList(List<Message> messages);
}
