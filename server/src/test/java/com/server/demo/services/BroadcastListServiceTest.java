package com.server.demo.services;

import static org.instancio.Select.field;

import com.server.demo.dtos.*;
import com.server.demo.models.BroadcastList;
import com.server.demo.models.Chat;
import com.server.demo.mappers.BroadcastListMapper;
import com.server.demo.mappers.ChatMapper;
import com.server.demo.repositories.BroadcastListRepository;
import com.server.demo.repositories.ChatRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    void setUp() {
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

        BroadcastListDTO data = broadcastListService.createList(requestDTO);

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

        when(broadcastListRepository.findById(id)).thenReturn(Optional.of(list));
        when(broadcastListMapper.toDTO(list)).thenReturn(responseDTO);

        BroadcastListDTO data = broadcastListService.getListById(id);

        assertEquals(responseDTO, data);
        verify(broadcastListRepository).findById(id);
        verify(broadcastListMapper).toDTO(list);
    }

    @Test
    @DisplayName("Buscar lista por ID com ID inválido deve falhar")
    void getListByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        when(broadcastListRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> broadcastListService.getListById(id));
        verify(broadcastListRepository).findById(id);
        verify(broadcastListMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Adicionar chat à lista com dados válidos")
    void addChatWithValidData() {
        UUID listId = UUID.randomUUID();
        AddChatToBroadcastListDTO chatDTO = Instancio.create(AddChatToBroadcastListDTO.class);
        BroadcastList list = new BroadcastList();
        list.setChats(new ArrayList<>());
        Chat chat = Instancio.create(Chat.class);
        BroadcastListDTO responseDTO = Instancio.create(BroadcastListDTO.class);

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.of(list));
        when(chatRepository.findById(chatDTO.getChatId())).thenReturn(Optional.of(chat));
        when(broadcastListRepository.save(list)).thenReturn(list);
        when(broadcastListMapper.toDTO(list)).thenReturn(responseDTO);

        BroadcastListDTO data = broadcastListService.addChat(listId, chatDTO);

        assertEquals(responseDTO, data);
        verify(broadcastListRepository).findById(listId);
        verify(chatRepository).findById(chatDTO.getChatId());
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

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.of(list));
        when(chatRepository.findById(chatId)).thenReturn(Optional.of(chat));
        when(broadcastListRepository.save(list)).thenReturn(list);
        when(broadcastListMapper.toDTO(list)).thenReturn(responseDTO);

        BroadcastListDTO data = broadcastListService.removeChat(listId, chatDTO);

        assertEquals(responseDTO, data);
        assertTrue(list.getChats().isEmpty());
        verify(broadcastListRepository).findById(listId);
        verify(chatRepository).findById(chatId);
        verify(broadcastListRepository).save(list);
    }

    @Test
    @DisplayName("Deletar lista com ID válido")
    void deleteListWithValidId() {
        UUID listId = UUID.randomUUID();

        doNothing().when(broadcastListRepository).deleteById(listId);

        assertDoesNotThrow(() -> broadcastListService.deleteList(listId));

        verify(broadcastListRepository).deleteById(listId);
    }

    @Test
    @DisplayName("Buscar todas as listas")
    void getAllLists() {
        List<BroadcastList> lists = List.of(Instancio.create(BroadcastList.class));
        List<BroadcastListDTO> responseDTOs = List.of(Instancio.create(BroadcastListDTO.class));

        when(broadcastListRepository.findAll()).thenReturn(lists);
        when(broadcastListMapper.toDTOList(lists)).thenReturn(responseDTOs);

        List<BroadcastListDTO> data = broadcastListService.getAllLists();

        assertEquals(responseDTOs, data);
        verify(broadcastListRepository).findAll();
        verify(broadcastListMapper).toDTOList(lists);
    }

    @Test
    @DisplayName("Buscar listas por ID do dono")
    void getListsByOwnerId() {
        UUID ownerId = UUID.randomUUID();
        List<BroadcastList> lists = List.of(Instancio.create(BroadcastList.class));
        List<BroadcastListDTO> responseDTOs = List.of(Instancio.create(BroadcastListDTO.class));

        when(broadcastListRepository.findAllByUserId(ownerId)).thenReturn(lists);
        when(broadcastListMapper.toDTOList(lists)).thenReturn(responseDTOs);

        List<BroadcastListDTO> data = broadcastListService.findAllByUserId(ownerId);

        assertEquals(responseDTOs, data);
        verify(broadcastListRepository).findAllByUserId(ownerId);
        verify(broadcastListMapper).toDTOList(lists);
    }

    @Test
    @DisplayName("Adicionar chat à lista com ID de lista inválido deve falhar")
    void addChatWithInvalidListId() {
        UUID listId = UUID.randomUUID();
        AddChatToBroadcastListDTO chatDTO = Instancio.create(AddChatToBroadcastListDTO.class);

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> broadcastListService.addChat(listId, chatDTO));

        verify(broadcastListRepository).findById(listId);
        verify(chatRepository, never()).findById(any());
        verify(broadcastListRepository, never()).save(any());
    }

    @Test
    @DisplayName("Adicionar chat à lista com ID de chat inválido deve falhar")
    void addChatWithInvalidChatId() {
        UUID listId = UUID.randomUUID();
        AddChatToBroadcastListDTO chatDTO = Instancio.create(AddChatToBroadcastListDTO.class);
        BroadcastList list = Instancio.create(BroadcastList.class);

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.of(list));
        when(chatRepository.findById(chatDTO.getChatId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> broadcastListService.addChat(listId, chatDTO));

        verify(broadcastListRepository).findById(listId);
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

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.of(list));
        when(chatMapper.toDTOList(chats)).thenReturn(responseDTOs);

        List<ChatDTO> data = broadcastListService.getChatsFromList(listId);

        assertEquals(responseDTOs, data);
        verify(broadcastListRepository).findById(listId);
        verify(chatMapper).toDTOList(chats);
    }

    @Test
    @DisplayName("Atualizar lista com dados válidos")
    void updateListWithValidData() {
        UUID listId = UUID.randomUUID();
        BroadcastList existingList = Instancio.create(BroadcastList.class);
        UpdateBroadcastListDTO requestDTO = Instancio.create(UpdateBroadcastListDTO.class);
        BroadcastListDTO responseDTO = Instancio.create(BroadcastListDTO.class);

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.of(existingList));
        when(broadcastListRepository.save(existingList)).thenReturn(existingList);
        when(broadcastListMapper.toDTO(existingList)).thenReturn(responseDTO);

        BroadcastListDTO data = broadcastListService.updateList(listId, requestDTO);

        assertEquals(responseDTO, data);
        verify(broadcastListRepository).findById(listId);
        verify(broadcastListRepository).save(existingList);

    }

    @Test
    @DisplayName("Atualizar lista com ID inválido deve falhar")
    void updateListWithInvalidId() {
        UUID listId = UUID.randomUUID();
        UpdateBroadcastListDTO requestDTO = Instancio.create(UpdateBroadcastListDTO.class);

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> broadcastListService.updateList(listId, requestDTO));
        
        assertEquals(String.format("Lista de transmissão com id %s não encontrada", listId), exception.getMessage());
        verify(broadcastListRepository).findById(listId);
        verify(broadcastListRepository, never()).save(any());
    }

    @Test
    @DisplayName("Remover chat da lista com ID de lista inválido deve falhar")
    void removeChatWithInvalidListId() {
        UUID listId = UUID.randomUUID();
        AddChatToBroadcastListDTO chatDTO = Instancio.create(AddChatToBroadcastListDTO.class);

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> broadcastListService.removeChat(listId, chatDTO));
        
        assertEquals(String.format("Lista de transmissão com id %s não encontrada", listId), exception.getMessage());
        verify(broadcastListRepository).findById(listId);
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

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.of(list));
        when(chatRepository.findById(chatId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> broadcastListService.removeChat(listId, chatDTO));
        
        assertEquals(String.format("Chat com id %s não encontrado", chatId), exception.getMessage());
        verify(broadcastListRepository).findById(listId);
        verify(chatRepository).findById(chatId);
        verify(broadcastListRepository, never()).save(any());
    }

    @Test
    @DisplayName("Buscar chats de uma lista com ID inválido deve falhar")
    void getChatsFromListWithInvalidId() {
        UUID listId = UUID.randomUUID();

        when(broadcastListRepository.findById(listId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> broadcastListService.getChatsFromList(listId));
        
        assertEquals(String.format("Lista de transmissão com id %s não encontrada", listId), exception.getMessage());
        verify(broadcastListRepository).findById(listId);
        verify(chatMapper, never()).toDTOList(any());
    }
}