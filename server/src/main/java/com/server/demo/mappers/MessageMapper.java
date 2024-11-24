package com.server.demo.mappers;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.models.Message;
import org.springframework.stereotype.Service;

@Service
public class MessageMapper {

    public MessageDTO toDTO(Message message) {
        return new MessageDTO(
            message.getId(), 
            message.getContent(), 
            message.getLastSentAt(), 
            message.getTimesSent(), 
            message.getOwner().getId(),
            message.getBroadcastList() != null ? message.getBroadcastList().getId() : null, 
            message.getChatRecipient() != null ? message.getChatRecipient().getId() : null 
        );
    }
}
