package com.server.demo.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.models.Message;
import com.server.demo.models.User;
import com.server.demo.repositories.UserRepository;

@Service
public class MessageMapper {

    @Autowired
    private UserRepository userRepository;

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

    public Message toEntity(RequestMessageDTO messageDTO) {
        Message message = new Message();
        message.setId(messageDTO.getId());
        message.setContent(messageDTO.getContent());
        User owner = userRepository.findById(messageDTO.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found with id: " + messageDTO.getOwnerId()));
        message.setOwner(owner);
        return message;
    }
}
