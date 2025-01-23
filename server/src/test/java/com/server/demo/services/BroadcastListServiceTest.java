package com.server.demo.services;

import com.server.demo.dtos.*;
import com.server.demo.models.BroadcastList;
import com.server.demo.models.Chat;
import com.server.demo.mappers.BroadcastListMapper;
import com.server.demo.repositories.BroadcastListRepository;
import com.server.demo.repositories.ChatRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BroadcastListServiceTest {

    @Mock
    private BroadcastListRepository broadcastListRepository;

    @Mock
    private ChatRepository chatRepository;

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
}
