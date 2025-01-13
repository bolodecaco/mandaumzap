package com.server.demo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.mappers.MessageMapper;
import com.server.demo.models.Message;
import com.server.demo.repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    public MessageDTO sendMessage(UUID messageId, RequestMessageDTO requestMessage) {
        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException(String.format("Lista de transmissão com id %s não encontrada", messageId)));
        if (message.getDeletedAt() != null) {
            throw new IllegalArgumentException("Não é possível enviar uma mensagem que foi deletada.");
        }

        message.setTimesSent(message.getTimesSent() + 1);
        message.setLastSentAt(new Date());

        Message currentMessage = messageRepository.save(message);

        return messageMapper.toDTO(currentMessage);
    }

    public Optional<MessageDTO> getMessageById(UUID id) {
        Optional<Message> currentMessage = messageRepository.findById(id);
        return currentMessage.map(messageMapper::toDTO);
    }

    public List<MessageDTO> getMessagesByOwnerId(UUID ownerId) {
        List<Message> messages = messageRepository.findByOwnerId(ownerId);
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
            .orElseThrow(() -> new RuntimeException(String.format("Lista de transmissão com id %s não encontrada", messageId)));

        if (message.getDeletedAt() != null) {
            message.setDeletedAt(new Date());
            messageRepository.save(message);
        } else {
            throw new IllegalArgumentException("Mensagem já foi deletada.");
        }
    }
}
