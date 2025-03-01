package com.server.demo.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.instancio.Instancio;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.server.demo.dtos.AddChatToBroadcastListDTO;
import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestBroadcastListDTO;
import com.server.demo.dtos.UpdateBroadcastListDTO;
import com.server.demo.mappers.BroadcastListMapper;
import com.server.demo.mappers.ChatMapper;
import com.server.demo.models.BroadcastList;
import com.server.demo.models.Chat;
import com.server.demo.repositories.BroadcastListRepository;
import com.server.demo.repositories.ChatRepository;

class BroadcastListServiceTest {

    @Mock
    private BroadcastListRepository broadcastListRepository;

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatMapper chatMapper;

    @Mock
    private BroadcastListMapper broadcastListMapper;

    @InjectMocks
    private BroadcastListService broadcastListService;

    private String userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Criar lista de transmissão com dados válidos")
    void createListWithValidData() {
        RequestBroadcastListDTO requestDTO = Instancio.create(RequestBroadcastListDTO.class);
        BroadcastList list = Instancio.create(BroadcastList.class);
        BroadcastListDTO responseDTO = Instancio.create(BroadcastListDTO.class);

        when(broadcastListMapper.toEntity(requestDTO)).thenReturn(list);
        when(broadcastListRepository.save(list)).thenReturn(list);
        when(broadcastListMapper.toDTO(list)).thenReturn(responseDTO);

        BroadcastListDTO data = broadcastListService.createList(requestDTO, userId);

        assertEquals(responseDTO, data);
        verify(broadcastListMapper).toEntity(requestDTO);
        verify(broadcastListRepository).save(list);
        verify(broadcastListMapper).toDTO(list);
    }

    @Test
    @DisplayName("Buscar lista por ID com ID válido")
    void getListByIdWithValidId() {
        UUID id = UUID.randomUUID();
        BroadcastList list = Instancio.create(BroadcastList.class);
        BroadcastListDTO responseDTO = Instancio.create(BroadcastListDTO.class);

        when(broadcastListRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.of(list));
        when(broadcastListMapper.toDTO(list)).thenReturn(responseDTO);

        BroadcastListDTO data = broadcastListService.getListById(id, userId);

        assertEquals(responseDTO, data);
        verify(broadcastListRepository).findByIdAndUserId(id, userId);
        verify(broadcastListMapper).toDTO(list);
    }

    @Test
    @DisplayName("Buscar lista por ID com ID inválido deve falhar")
    void getListByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        when(broadcastListRepository.findByIdAndUserId(id, userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> broadcastListService.getListById(id, userId));
        verify(broadcastListRepository).findByIdAndUserId(id, userId);
        verify(broadcastListMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Adicionar chat à lista com dados válidos")
    void addChatWithValidData() {
        UUID listId = UUID.randomUUID();
        List<AddChatToBroadcastListDTO> chatsDTO = List.of(Instancio.create(AddChatToBroadcastListDTO.class));
        BroadcastList list = new BroadcastList();
        list.setChats(new ArrayList<>());
        Chat chat = Instancio.create(Chat.class);
        BroadcastListDTO responseDTO = Instancio.create(BroadcastListDTO.class);

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.of(list));
        when(chatRepository.findById(chatsDTO.get(0).getChatId())).thenReturn(Optional.of(chat));
        when(broadcastListRepository.save(list)).thenReturn(list);
        when(broadcastListMapper.toDTO(list)).thenReturn(responseDTO);

        BroadcastListDTO data = broadcastListService.addChats(listId, chatsDTO, userId
        );

        assertEquals(responseDTO, data);
        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(broadcastListRepository).save(list);
    }

    @Test
    @DisplayName("Remover chat da lista com dados válidos")
    void removeChatWithValidData() {
        UUID listId = UUID.randomUUID();
        UUID chatId = UUID.randomUUID();
        BroadcastList list = new BroadcastList();
        Chat chat = Instancio.create(Chat.class);
        AddChatToBroadcastListDTO chatDTO = Instancio.of(AddChatToBroadcastListDTO.class)
                .set(field(AddChatToBroadcastListDTO::getChatId), chatId)
                .create();
        chat.setId(chatId);
        list.setChats(new ArrayList<>(List.of(chat)));
        BroadcastListDTO responseDTO = Instancio.create(BroadcastListDTO.class);

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.of(list));
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));
        when(broadcastListRepository.save(list)).thenReturn(list);
        when(broadcastListMapper.toDTO(list)).thenReturn(responseDTO);

        BroadcastListDTO data = broadcastListService.removeChat(listId, chatDTO, userId);

        assertEquals(responseDTO, data);
        assertTrue(list.getChats().isEmpty());
        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(chatRepository).findById(chatId);
        verify(broadcastListRepository).save(list);
    }

    @Test
    @DisplayName("Buscar todas as listas")
    void getAllLists() {
        List<BroadcastList> lists = List.of(Instancio.create(BroadcastList.class));
        List<BroadcastListDTO> responseDTOs = List.of(Instancio.create(BroadcastListDTO.class));
        Pageable pageable = Pageable.unpaged();
        Page<BroadcastList> page = new PageImpl<>(lists);

        when(broadcastListRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(broadcastListMapper.toDTO(any(BroadcastList.class))).thenReturn(responseDTOs.get(0));

        Page<BroadcastListDTO> data = broadcastListService.findAllByUserId(userId, pageable, "titulo");

        assertEquals(1, data.getContent().size());
        verify(broadcastListRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Buscar listas por ID do dono")
    void getListsByOwnerId() {
        List<BroadcastList> lists = List.of(Instancio.create(BroadcastList.class));
        List<BroadcastListDTO> responseDTOs = List.of(Instancio.create(BroadcastListDTO.class));
        Pageable pageable = Pageable.unpaged();
        Page<BroadcastList> page = new PageImpl<>(lists);

        when(broadcastListRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);
        when(broadcastListMapper.toDTO(any(BroadcastList.class))).thenReturn(responseDTOs.get(0));

        Page<BroadcastListDTO> data = broadcastListService.findAllByUserId(userId, pageable, "titulo");

        assertEquals(1, data.getContent().size());
        verify(broadcastListRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Adicionar chat à lista com ID de lista inválido deve falhar")
    void addChatWithInvalidListId() {
        UUID listId = UUID.randomUUID();
        List<AddChatToBroadcastListDTO> chatsDTO = List.of(Instancio.create(AddChatToBroadcastListDTO.class));

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> broadcastListService.addChats(listId, chatsDTO, userId));

        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(chatRepository, never()).findById(any());
        verify(broadcastListRepository, never()).save(any());
    }

    @Test
    @DisplayName("Adicionar chat à lista com ID de chat inválido deve falhar")
    void addChatWithInvalidChatId() {
        UUID listId = UUID.randomUUID();
        AddChatToBroadcastListDTO chatDTO = Instancio.create(AddChatToBroadcastListDTO.class);
        BroadcastList list = Instancio.create(BroadcastList.class);
        List<AddChatToBroadcastListDTO> chatsDto = List.of(chatDTO);

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.of(list));
        when(chatRepository.findById(chatDTO.getChatId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> broadcastListService.addChats(listId, chatsDto, userId));

        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(chatRepository).findById(chatDTO.getChatId());
        verify(broadcastListRepository, never()).save(any());
    }

    @Test
    @DisplayName("Buscar chats de uma lista com ID válido")
    void getChatsFromList() {
        UUID listId = UUID.randomUUID();
        BroadcastList list = new BroadcastList();
        List<Chat> chats = List.of(Instancio.create(Chat.class));
        list.setChats(new ArrayList<>(chats));
        List<ChatDTO> responseDTOs = List.of(Instancio.create(ChatDTO.class));

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.of(list));
        when(chatMapper.toDTOList(chats)).thenReturn(responseDTOs);

        List<ChatDTO> data = broadcastListService.getChatsFromList(listId, userId);

        assertEquals(responseDTOs, data);
        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(chatMapper).toDTOList(chats);
    }

    @Test
    @DisplayName("Atualizar lista com dados válidos")
    void updateListWithValidData() {
        UUID listId = UUID.randomUUID();
        BroadcastList existingList = Instancio.create(BroadcastList.class);
        UpdateBroadcastListDTO requestDTO = Instancio.create(UpdateBroadcastListDTO.class);
        BroadcastListDTO responseDTO = Instancio.create(BroadcastListDTO.class);

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.of(existingList));
        when(broadcastListRepository.save(existingList)).thenReturn(existingList);
        when(broadcastListMapper.toDTO(existingList)).thenReturn(responseDTO);

        BroadcastListDTO data = broadcastListService.updateList(listId, requestDTO, userId);

        assertEquals(responseDTO, data);
        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(broadcastListRepository).save(existingList);

    }

    @Test
    @DisplayName("Atualizar lista com ID inválido deve falhar")
    void updateListWithInvalidId() {
        UUID listId = UUID.randomUUID();
        UpdateBroadcastListDTO requestDTO = Instancio.create(UpdateBroadcastListDTO.class);

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> broadcastListService.updateList(listId, requestDTO, userId));

        assertEquals(String.format("Lista de transmissão com id %s não encontrada", listId), exception.getMessage());
        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(broadcastListRepository, never()).save(any());
    }

    @Test
    @DisplayName("Remover chat da lista com ID de lista inválido deve falhar")
    void removeChatWithInvalidListId() {
        UUID listId = UUID.randomUUID();
        AddChatToBroadcastListDTO chatDTO = Instancio.create(AddChatToBroadcastListDTO.class);

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> broadcastListService.removeChat(listId, chatDTO, userId));

        assertEquals(String.format("Lista de transmissão com id %s não encontrada", listId), exception.getMessage());
        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(chatRepository, never()).findById(any());
    }

    @Test
    @DisplayName("Remover chat da lista com ID de chat inválido deve falhar")
    void removeChatWithInvalidChatId() {
        UUID listId = UUID.randomUUID();
        UUID chatId = UUID.randomUUID();
        AddChatToBroadcastListDTO chatDTO = Instancio.of(AddChatToBroadcastListDTO.class)
                .set(field(AddChatToBroadcastListDTO::getChatId), chatId)
                .create();
        BroadcastList list = Instancio.create(BroadcastList.class);

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.of(list));
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> broadcastListService.removeChat(listId, chatDTO, userId));

        assertEquals(String.format("Chat com id %s não encontrado", chatId), exception.getMessage());
        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(chatRepository).findById(chatId);
        verify(broadcastListRepository, never()).save(any());
    }

    @Test
    @DisplayName("Buscar chats de uma lista com ID inválido deve falhar")
    void getChatsFromListWithInvalidId() {
        UUID listId = UUID.randomUUID();

        when(broadcastListRepository.findByIdAndUserId(listId, userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> broadcastListService.getChatsFromList(listId, userId));

        assertEquals(String.format("Lista de transmissão com id %s não encontrada", listId), exception.getMessage());
        verify(broadcastListRepository).findByIdAndUserId(listId, userId);
        verify(chatMapper, never()).toDTOList(any());
    }
}
