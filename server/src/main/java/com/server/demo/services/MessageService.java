package com.server.demo.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.MessageSentToBotDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.exception.BusinessException;
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

    @Autowired
    private BroadcastListService broadcastListService;

    public MessageDTO sendMessage(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new BusinessException(String.format("Mensagem com id %s não encontrada", messageId)));
    
        if (message.getDeletedAt() != null) {
            throw new BusinessException("Não é possível enviar uma mensagem que foi deletada.");
        }
    
        message.setTimesSent(message.getTimesSent() + 1);
        message.setLastSentAt(new Date());

        List<String> receiverIds = broadcastListService.getChatsFromList(message.getBroadcastList().getId())
        .stream()
        .map(ChatDTO::getWhatsAppId)
        .toList();   

        MessageSentToBotDTO messageToBeSent = MessageSentToBotDTO.builder()
                .sessionId(message.getSession().getId())
                .userId(message.getSession().getUser().getId())
                .text(message.getContent())
                .receivers(receiverIds)
                .build();
    
        Message currentMessage = messageRepository.save(message);
        MessageDTO messageDTO = messageMapper.toDTO(currentMessage);
        
        messageProducer.sendObject(messageToBeSent);
    
        return messageDTO;
    }

    public MessageDTO getMessageById(UUID id) {
        Message currentMessage = messageRepository.findById(id)
                .orElseThrow(() -> new BusinessException(String.format("Mensagem com id %s não encontrada", id)));

        return messageMapper.toDTO(currentMessage);
    }

    public List<MessageDTO> getMessagesBySessionId(UUID userId) {
        List<Message> messages = messageRepository.findBySessionId(userId);
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
                .orElseThrow(() -> new BusinessException(String.format("Mensagem com id %s não encontrada", messageId)));

        message.setDeletedAt(new Date());
        messageRepository.save(message);
    }
}
