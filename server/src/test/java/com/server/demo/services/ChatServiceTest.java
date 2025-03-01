package com.server.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.instancio.Instancio;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.server.demo.dtos.BotChatsDTO;
import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
import com.server.demo.mappers.ChatMapper;
import com.server.demo.models.Chat;
import com.server.demo.models.Session;
import com.server.demo.repositories.ChatRepository;
import com.server.demo.repositories.SessionRepository;

class ChatServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private ChatMapper chatMapper;

    @InjectMocks
    private ChatService chatService;

    private String userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
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

        ChatDTO data = chatService.createChat(requestDTO, userId);

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

        when(chatRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(chat));
        when(chatMapper.toDTO(chat)).thenReturn(responseDTO);

        ChatDTO data = chatService.getChatDTOById(id, userId);

        assertEquals(responseDTO, data);
        verify(chatRepository).findByIdAndUserId(id, userId);
        verify(chatMapper).toDTO(chat);
    }

    @Test
    @DisplayName("Atualizar chat com dados válidos")
    void updateChatWithValidData() {
        UUID id = UUID.randomUUID();
        Chat existingChat = Instancio.create(Chat.class);
        UpdateChatDTO updateDTO = Instancio.create(UpdateChatDTO.class);
        ChatDTO responseDTO = Instancio.create(ChatDTO.class);

        when(chatRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(existingChat));
        when(chatRepository.save(existingChat)).thenReturn(existingChat);
        when(chatMapper.toDTO(existingChat)).thenReturn(responseDTO);

        ChatDTO data = chatService.updateChat(id, updateDTO, userId);

        assertEquals(responseDTO, data);
        verify(chatRepository).findByIdAndUserId(id, userId);
        verify(chatRepository).save(existingChat);
    }

    @Test
    @DisplayName("Atualizar chat com ID inválido deve falhar")
    void updateChatWithInvalidId() {
        UUID id = UUID.randomUUID();
        UpdateChatDTO updateChatDTO = Instancio.create(UpdateChatDTO.class);

        when(chatRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> chatService.updateChat(id, updateChatDTO, userId));
        verify(chatRepository).findByIdAndUserId(id, userId);
        verifyNoMoreInteractions(chatRepository);
        verifyNoInteractions(chatMapper);
    }

    @Test
    @DisplayName("Deletar chat com ID válido")
    void deleteChatWithValidId() {
        UUID id = UUID.randomUUID();
        Chat chat = Instancio.create(Chat.class);

        when(chatRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(chat));

        chatService.deleteChat(id, userId);

        verify(chatRepository).delete(chat);
    }

    @Test
    @DisplayName("Buscar chat por ID com ID inválido deve falhar")
    void getChatByIdWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(chatRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> chatService.getChatById(id, userId));
        verify(chatRepository).findByIdAndUserId(id, userId);
    }

    @Test
    @DisplayName("Buscar chat por ID com ID válido retorna entidade")
    void getChatByIdReturnsEntity() {
        UUID id = UUID.randomUUID();
        Chat expectedChat = Instancio.create(Chat.class);

        when(chatRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(expectedChat));

        Chat result = chatService.getChatById(id, userId);

        assertEquals(expectedChat, result);
        verify(chatRepository).findByIdAndUserId(id, userId);
    }

    @Test
    @DisplayName("Buscar chat DTO por ID com ID inválido deve falhar")
    void getChatDTOByIdWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(chatRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> chatService.getChatDTOById(id, userId));

        assertEquals(String.format("Chat com id %s não encontrado", id), exception.getMessage());
        verify(chatRepository).findByIdAndUserId(id, userId);
        verify(chatMapper, never()).toDTO(any());
    }

    @SuppressWarnings("unchecked")
    @Test
    @DisplayName("Buscar todos os chats com parâmetros válidos")
    void getAllChatsWithValidParameters() {
        Pageable pageable = PageRequest.of(0, 10);
        String search = "chatName";
        String sessionId = UUID.randomUUID().toString();
        
        Chat chat = Instancio.create(Chat.class);
        ChatDTO chatDTO = Instancio.create(ChatDTO.class);
        
        Page<Chat> chatPage = new PageImpl<>(List.of(chat), pageable, 1);
        
        when(chatRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(chatPage);
        when(chatMapper.toDTO(any(Chat.class))).thenReturn(chatDTO);

        Page<ChatDTO> chats = chatService.getAllChats(userId, pageable, search, sessionId);

        assertNotNull(chats);
        assertEquals(1, chats.getTotalElements());
        assertEquals(chatDTO, chats.getContent().get(0));
        verify(chatRepository).findAll(any(Specification.class), any(Pageable.class));
        verify(chatMapper).toDTO(any(Chat.class));
    }

    @Test
    @DisplayName("Inserir chats novos")
    void insertChatsWithUniqueChats() {
        UUID sessionId = UUID.randomUUID();
        
        BotChatsDTO.BotResponseChats chat1 = Instancio.create(BotChatsDTO.BotResponseChats.class);
        chat1.setId("chatId1");
    
        BotChatsDTO.BotResponseChats chat2 = Instancio.create(BotChatsDTO.BotResponseChats.class);
        chat2.setId("chatId2");
        
        List<BotChatsDTO.BotResponseChats> botChats = List.of(chat1, chat2);
    
        Session session = Instancio.create(Session.class);
        session.setUserId(userId);
        
        Chat existingChat = Instancio.create(Chat.class);
        existingChat.setWhatsAppId(chat1.getId());
    
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(chatRepository.findByWhatsAppId(chat1.getId())).thenReturn(Optional.of(existingChat));
        when(chatRepository.findByWhatsAppId(chat2.getId())).thenReturn(Optional.empty());
    
        chatService.insertChats(botChats, sessionId);
    
        verify(sessionRepository).findById(sessionId);
        verify(chatRepository).findByWhatsAppId(chat1.getId());
        verify(chatRepository).findByWhatsAppId(chat2.getId());
        verify(chatRepository).saveAll(anyList());
    }
}