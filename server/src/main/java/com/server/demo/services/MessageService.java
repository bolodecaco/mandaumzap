package com.server.demo.services;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.mappers.MessageMapper;
import com.server.demo.models.Chat;
import com.server.demo.models.Message;
import com.server.demo.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.List;

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

        Chat chatRecipient = chatService.getChatById(requestMessage.getChatRecipientId());
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
        return messageRepository.findByOwnerId(ownerId).stream()
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
    }

    public MessageDTO saveMessage(Message message) {
        Message currentMessage = messageRepository.save(message);
        return messageMapper.toDTO(currentMessage);
    }

    public List<MessageDTO> getActiveMessages() {
        return messageRepository.findByDeletedAtIsNull().stream()
                .map(messageMapper::toDTO)
                .collect(Collectors.toList());
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
