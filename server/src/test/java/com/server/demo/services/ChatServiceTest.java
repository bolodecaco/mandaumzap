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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
    @DisplayName("Atualizar chat com ID inválido deve falhar")
    void updateChatWithInvalidId() {
        UUID id = UUID.randomUUID();
        UpdateChatDTO updateChatDTO = Instancio.create(UpdateChatDTO.class);
        
        when(chatRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> chatService.updateChat(id, updateChatDTO));
        verify(chatRepository).findById(id);
        verifyNoMoreInteractions(chatRepository);
        verifyNoInteractions(chatMapper);
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

    @Test
    @DisplayName("Listar todos os chats")
    void getAllChats() {
        List<Chat> chats = List.of(Instancio.create(Chat.class));
        List<ChatDTO> responseDTOs = List.of(Instancio.create(ChatDTO.class));

        when(chatRepository.findAll()).thenReturn(chats);
        when(chatMapper.toDTOList(chats)).thenReturn(responseDTOs);

        List<ChatDTO> result = chatService.getAllChats();

        assertEquals(responseDTOs.size(), result.size());
        verify(chatRepository).findAll();
        verify(chatMapper).toDTOList(chats);
    }

    @Test
    @DisplayName("Buscar chat por ID com ID inválido deve falhar")
    void getChatByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        
        when(chatRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> chatService.getChatById(id));
        verify(chatRepository).findById(id);
    }

    @Test
    @DisplayName("Buscar chat por ID com ID válido retorna entidade")
    void getChatByIdReturnsEntity() {
        UUID id = UUID.randomUUID();
        Chat expectedChat = Instancio.create(Chat.class);

        when(chatRepository.findById(id)).thenReturn(Optional.of(expectedChat));

        Chat result = chatService.getChatById(id);

        assertEquals(expectedChat, result);
        verify(chatRepository).findById(id);
    }

    @Test
    @DisplayName("Buscar chat DTO por ID com ID inválido deve falhar")
    void getChatDTOByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        
        when(chatRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> chatService.getChatDTOById(id));
        
        assertEquals(String.format("Chat com id %s não encontrado", id), exception.getMessage());
        verify(chatRepository).findById(id);
        verify(chatMapper, never()).toDTO(any());
    }
}