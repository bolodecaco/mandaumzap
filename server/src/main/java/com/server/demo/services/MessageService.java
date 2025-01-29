package com.server.demo.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.mappers.MessageMapper;
import com.server.demo.models.Message;
import com.server.demo.producer.MessageProducer;
import com.server.demo.repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    public MessageDTO sendMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException(String.format("Mensagem com id %s não encontrada", messageId)));
        if (message.getDeletedAt() != null) {
            throw new IllegalArgumentException("Não é possível enviar uma mensagem que foi deletada.");
        }
        message.setTimesSent(message.getTimesSent() + 1);
        message.setLastSentAt(new Date());
        Message currentMessage = messageRepository.save(message);
        MessageDTO messageDTO = messageMapper.toDTO(currentMessage);
        messageProducer.sendObject(messageDTO);
        return messageMapper.toDTO(currentMessage);
    }

    public MessageDTO getMessageById(UUID id) {
        Message currentMessage = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Mensagem com id %s não encontrada", id)));

        return messageMapper.toDTO(currentMessage);
    }

    public List<MessageDTO> getMessagesByUserId(UUID userId) {
        List<Message> messages = messageRepository.findByOwnerId(userId);
        return messageMapper.toDTOList(messages);
    }

    public MessageDTO saveMessage(RequestMessageDTO message) {
        Message currentMessage = messageMapper.toEntity(message);
        messageRepository.save(currentMessage);
        return messageMapper.toDTO(currentMessage);
    }

    public List<MessageDTO> getActiveMessages() {
        List<Message> messages = messageRepository.findByDeletedAtIsNull();
        return messageMapper.toDTOList(messages);
    }

    public void deleteMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException(String.format("Mensagem com id %s não encontrada", messageId)));

        message.setDeletedAt(new Date());
        messageRepository.save(message);
    }
}
