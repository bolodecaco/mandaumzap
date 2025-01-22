package com.server.demo.services;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.server.demo.dtos.AddChatToBroadcastListDTO;
import com.server.demo.dtos.RequestBroadcastListDTO;
import com.server.demo.dtos.UpdateBroadcastListDTO;
import com.server.demo.mappers.BroadcastListMapper;
import com.server.demo.models.BroadcastList;
import com.server.demo.models.Chat;
import com.server.demo.repositories.BroadcastListRepository;
import com.server.demo.repositories.ChatRepository;

class BroadcastListServiceTest {

    @Mock
    BroadcastListRepository broadcastListRepository;

    @Mock
    BroadcastListMapper broadcastListMapper;

    @Mock
    ChatRepository chatRepository;

    @InjectMocks
    BroadcastListService broadcastListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test save broadcast list")
    void createBroadcastList() {
        RequestBroadcastListDTO requesBroadcastListDTO = new RequestBroadcastListDTO();
        requesBroadcastListDTO.setTitle("Minha lista");
        BroadcastList message = new BroadcastList();
        when(broadcastListMapper.toEntity(requesBroadcastListDTO)).thenReturn(message);
        broadcastListService.createList(requesBroadcastListDTO);
        assertNotNull(message);
    }

    @Test
    @DisplayName("get list by id")
    void getListById() {
        UUID id = UUID.randomUUID();
        when(broadcastListRepository.findById(id)).thenReturn(java.util.Optional.of(new BroadcastList()));
        broadcastListService.getListById(id);
        verify(broadcastListRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("update list")
    void updateList() {
        UUID id = UUID.randomUUID();
        BroadcastList broadcastList = new BroadcastList();
        broadcastList.setId(id);
        when(broadcastListRepository.findById(id)).thenReturn(java.util.Optional.of(broadcastList));
        when(broadcastListRepository.save(broadcastList)).thenReturn(broadcastList);
        broadcastList.setTitle("Minha lista atualizada");
        broadcastListService.updateList(id, new UpdateBroadcastListDTO());
        verify(broadcastListRepository, times(1)).save(broadcastList);
    }

    @Test
    @DisplayName("delete list")
    void deleteList() {
        UUID id = UUID.randomUUID();
        BroadcastList plan = new BroadcastList();
        when(broadcastListRepository.findById(id)).thenReturn(java.util.Optional.of(plan));
        broadcastListService.deleteList(id);
        verify(broadcastListRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("add chat to list")
    void addChatToList() {
        UUID id = UUID.randomUUID();
        UUID chatId = UUID.randomUUID();
        BroadcastList broadcastList = new BroadcastList();
        broadcastList.setChats(new ArrayList<>());
        AddChatToBroadcastListDTO chatDto = new AddChatToBroadcastListDTO();
        chatDto.setChatId(chatId);
        Chat chat = new Chat();
        chat.setId(chatId);
        when(broadcastListRepository.findById(id)).thenReturn(java.util.Optional.of(broadcastList));
        when(chatRepository.findById(chatId)).thenReturn(java.util.Optional.of(chat));
        when(broadcastListRepository.save(broadcastList)).thenReturn(broadcastList);
        broadcastListService.addChat(id, chatDto);
        verify(broadcastListRepository, times(1)).save(broadcastList);
        verify(chatRepository, times(1)).findById(chatId);
    }

    @Test
    @DisplayName("remove chat from list")
    void removeChatFromList() {
        UUID id = UUID.randomUUID();
        UUID chatId = UUID.randomUUID();
        BroadcastList broadcastList = new BroadcastList();
        broadcastList.setChats(new ArrayList<>());
        AddChatToBroadcastListDTO chatDto = new AddChatToBroadcastListDTO();
        chatDto.setChatId(chatId);
        Chat chat = new Chat();
        chat.setId(chatId);
        broadcastList.getChats().add(chat);
        when(broadcastListRepository.findById(id)).thenReturn(java.util.Optional.of(broadcastList));
        when(chatRepository.findById(chatId)).thenReturn(java.util.Optional.of(chat));
        when(broadcastListRepository.save(broadcastList)).thenReturn(broadcastList);
        broadcastListService.removeChat(id, chatDto);
        verify(broadcastListRepository, times(1)).save(broadcastList);
        verify(chatRepository, times(1)).findById(chatId);
    }

}
