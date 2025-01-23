package com.server.demo.services;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
import com.server.demo.mappers.ChatMapper;
import com.server.demo.models.Chat;
import com.server.demo.repositories.ChatRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Criar chat com dados válidos")
    void createChatWithValidData() {
        RequestChatDTO requestDTO = Instancio.create(RequestChatDTO.class);
        Chat chat = Instancio.create(Chat.class);
        ChatDTO responseDTO = Instancio.create(ChatDTO.class);

        when(chatMapper.toEntity(requestDTO)).thenReturn(chat);
        when(chatRepository.save(chat)).thenReturn(chat);
        when(chatMapper.toDTO(chat)).thenReturn(responseDTO);

        ChatDTO data = chatService.createChat(requestDTO);

        assertEquals(responseDTO, data);
        verify(chatMapper).toEntity(requestDTO);
        verify(chatRepository).save(chat);
        verify(chatMapper).toDTO(chat);
    }

    @Test
    @DisplayName("Buscar chat por ID com ID válido")
    void getChatByIdWithValidId() {
        UUID id = UUID.randomUUID();
        Chat chat = Instancio.create(Chat.class);
        ChatDTO responseDTO = Instancio.create(ChatDTO.class);

        when(chatRepository.findById(id)).thenReturn(Optional.of(chat));
        when(chatMapper.toDTO(chat)).thenReturn(responseDTO);

        ChatDTO data = chatService.getChatDTOById(id);

        assertEquals(responseDTO, data);
        verify(chatRepository).findById(id);
        verify(chatMapper).toDTO(chat);
    }

    @Test
    @DisplayName("Atualizar chat com dados válidos")
    void updateChatWithValidData() {
        UUID id = UUID.randomUUID();
        Chat existingChat = Instancio.create(Chat.class);
        UpdateChatDTO updateDTO = Instancio.create(UpdateChatDTO.class);
        ChatDTO responseDTO = Instancio.create(ChatDTO.class);

        when(chatRepository.findById(id)).thenReturn(Optional.of(existingChat));
        when(chatRepository.save(existingChat)).thenReturn(existingChat);
        when(chatMapper.toDTO(existingChat)).thenReturn(responseDTO);

        ChatDTO data = chatService.updateChat(id, updateDTO);

        assertEquals(responseDTO, data);
        verify(chatRepository).findById(id);
        verify(chatRepository).save(existingChat);
    }

    @Test
    @DisplayName("Deletar chat com ID válido")
    void deleteChatWithValidId() {
        UUID id = UUID.randomUUID();
        Chat chat = Instancio.create(Chat.class);

        when(chatRepository.findById(id)).thenReturn(Optional.of(chat));
        
        chatService.deleteChat(id);

        verify(chatRepository).deleteById(id);
    }
}
