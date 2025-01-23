package com.server.demo.services;


import static org.instancio.Select.field;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.models.Message;
import com.server.demo.mappers.MessageMapper;
import com.server.demo.repositories.MessageRepository;

import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Enviar mensagem com ID válido")
    void sendMessageWithValidId() {
        UUID messageId = UUID.randomUUID();
        
        Message message = Instancio.of(Message.class)
                .set(field(Message::getDeletedAt), null)
                .set(field(Message::getTimesSent), 0)
                .create();        
        RequestMessageDTO requestDTO = Instancio.create(RequestMessageDTO.class);
        MessageDTO responseDTO = Instancio.create(MessageDTO.class);
        
        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.save(message)).thenReturn(message);
        when(messageMapper.toDTO(message)).thenReturn(responseDTO);

        MessageDTO data = messageService.sendMessage(messageId, requestDTO);

        assertEquals(responseDTO, data);
        verify(messageRepository).findById(messageId);
        verify(messageRepository).save(message);
        assertTrue(message.getTimesSent() == 1);
        assertNotNull(message.getLastSentAt());
    }

    @Test
    @DisplayName("Buscar mensagem com ID válido")
    void getMessageWithValidId() {
        UUID messageId = UUID.randomUUID();
        Message message = Instancio.create(Message.class);
        MessageDTO responseDTO = Instancio.create(MessageDTO.class);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageMapper.toDTO(message)).thenReturn(responseDTO);

        MessageDTO data = messageService.getMessageById(messageId);

        assertEquals(responseDTO, data);
        verify(messageRepository).findById(messageId);
        verify(messageMapper).toDTO(message);
    }

    @Test
    @DisplayName("Criar nova mensagem com dados válidos")
    void createMessageWithValidData() {
        RequestMessageDTO requestDTO = Instancio.create(RequestMessageDTO.class);
        Message message = Instancio.create(Message.class);
        MessageDTO responseDTO = Instancio.create(MessageDTO.class);

        when(messageMapper.toEntity(requestDTO)).thenReturn(message);
        when(messageRepository.save(message)).thenReturn(message);
        when(messageMapper.toDTO(message)).thenReturn(responseDTO);

        MessageDTO data = messageService.saveMessage(requestDTO);

        assertEquals(responseDTO, data);
        verify(messageMapper).toEntity(requestDTO);
        verify(messageRepository).save(message);
        verify(messageMapper).toDTO(message);
    }

    @Test
    @DisplayName("Buscar mensagem com ID inválido deve falhar")
    void getMessageWithInvalidId() {
        UUID messageId = UUID.randomUUID();
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> messageService.getMessageById(messageId));
            
        verify(messageRepository).findById(messageId);
        verify(messageMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Buscar mensagens através de ID de usuário válido")
    void getMessagesByUserId() {
        UUID userId = UUID.randomUUID();
        List<Message> messages = List.of(Instancio.create(Message.class));
        List<MessageDTO> responseDTOs = List.of(Instancio.create(MessageDTO.class));

        when(messageRepository.findByOwnerId(userId)).thenReturn(messages);
        when(messageMapper.toDTOList(messages)).thenReturn(responseDTOs);

        List<MessageDTO> data = messageService.getMessagesByUserId(userId);

        assertEquals(responseDTOs, data);
        verify(messageRepository).findByOwnerId(userId);
        verify(messageMapper).toDTOList(messages);
    }

    @Test
    @DisplayName("Enviar mensagem com ID inválido deve falhar")
    void sendMessageWithInvalidId() {
        UUID messageId = UUID.randomUUID();
        RequestMessageDTO requestDTO = Instancio.create(RequestMessageDTO.class);
        
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> messageService.sendMessage(messageId, requestDTO));
            
        verify(messageRepository).findById(messageId);
        verify(messageRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deletar mensagem com ID inválido deve falhar")
    void deleteMessageWithInvalidId() {
        UUID messageId = UUID.randomUUID();
        when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> messageService.deleteMessage(messageId));
            
        verify(messageRepository).findById(messageId);
        verify(messageRepository, never()).save(any());
    }

    @Test
    @DisplayName("Enviar mensagem deletada deve falhar")
    void sendMessageWithDeletedAt() {
        UUID messageId = UUID.randomUUID();
        Message message = Instancio.create(Message.class);
        message.setDeletedAt(new Date());
        RequestMessageDTO requestDTO = Instancio.create(RequestMessageDTO.class);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

        assertThrows(RuntimeException.class, 
            () -> messageService.sendMessage(messageId, requestDTO));
        
        verify(messageRepository).findById(messageId);
        verify(messageRepository, never()).save(any());
    }

    @Test
    @DisplayName("Buscar mensagens ativas")
    void getActiveMessages() {
        List<Message> messages = List.of(Instancio.create(Message.class));
        List<MessageDTO> responseDTOs = List.of(Instancio.create(MessageDTO.class));

        when(messageRepository.findByDeletedAtIsNull()).thenReturn(messages);
        when(messageMapper.toDTOList(messages)).thenReturn(responseDTOs);

        List<MessageDTO> data = messageService.getActiveMessages();

        assertEquals(responseDTOs, data);
        verify(messageRepository).findByDeletedAtIsNull();
        verify(messageMapper).toDTOList(messages);
    }

    @Test
    @DisplayName("Deletar mensagem com ID válido")
    void deleteMessageWithValidId() {
        UUID messageId = UUID.randomUUID();
        
        Message message = Instancio.create(Message.class);

        when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));
        when(messageRepository.save(message)).thenReturn(message);

        messageService.deleteMessage(messageId);

        assertNotNull(message.getDeletedAt());
        verify(messageRepository).findById(messageId);
        verify(messageRepository).save(message);
    }
}