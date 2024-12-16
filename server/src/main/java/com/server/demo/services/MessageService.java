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
import com.server.demo.models.Chat;
import com.server.demo.models.Message;
import com.server.demo.repositories.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private ChatService chatService;

    public MessageDTO sendMessage(UUID messageId, RequestMessageDTO requestMessage) {
        Optional<Message> messageOptional = messageRepository.findById(messageId);

        if (messageOptional.isEmpty()) {
            throw new IllegalArgumentException("Mensagem não encontrada.");
        }

        Message message = messageOptional.get();

        if (message.isDeleted()) {
            throw new IllegalArgumentException("Não é possível enviar uma mensagem que foi deletada.");
        }

        Chat chatRecipient = chatService.getChatById(requestMessage.getChatId());
        message.setChatRecipient(chatRecipient);

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
        Optional<Message> messageOptional = messageRepository.findById(messageId);

        if (messageOptional.isEmpty()) {
            throw new IllegalArgumentException("Mensagem não encontrada.");
        }

        Message message = messageOptional.get();

        if (!message.isDeleted()) {
            message.softDelete();
            messageRepository.save(message);
        } else {
            throw new IllegalArgumentException("Mensagem já foi deletada.");
        }
    }
}
