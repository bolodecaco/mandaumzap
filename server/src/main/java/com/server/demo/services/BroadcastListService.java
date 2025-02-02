package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class BroadcastListService {

    @Autowired
    private BroadcastListRepository broadcastListRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private BroadcastListMapper broadcastListMapper;

    @Autowired
    private ChatMapper chatMapper;

    public List<BroadcastListDTO> findAllByUserId(UUID userId) {
        List<BroadcastList> list = broadcastListRepository.findAllByUserId(userId);
        return broadcastListMapper.toDTOList(list);
    }

    public List<BroadcastListDTO> getAllLists() {
        List<BroadcastList> list = broadcastListRepository.findAll();
        return broadcastListMapper.toDTOList(list);
    }

    public BroadcastListDTO getListById(UUID id) {
        BroadcastList list = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Lista de transmissão com id %s não encontrada", id)));
        return broadcastListMapper.toDTO(list);
    }

    public BroadcastListDTO createList(RequestBroadcastListDTO list) {
        BroadcastList currentList = broadcastListMapper.toEntity(list);
        broadcastListRepository.save(currentList);
        return broadcastListMapper.toDTO(currentList);
    }

    public BroadcastListDTO updateList(UUID id, UpdateBroadcastListDTO listDetails) {
        BroadcastList existingList = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Lista de transmissão com id %s não encontrada", id)));
        broadcastListMapper.updateEntityFromDTO(listDetails, existingList);
        broadcastListRepository.save(existingList);
        return broadcastListMapper.toDTO(existingList);
    }

    public void incrementMessageSent(UUID id) {
        BroadcastList list = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Lista de transmissão com id %s não encontrada", id)));
        list.setMessagesSent(list.getMessagesSent() + 1);
        broadcastListRepository.save(list);
    }

    public void deleteList(UUID id) {
        broadcastListRepository.deleteById(id);
    }

    public BroadcastListDTO addChat(UUID id, AddChatToBroadcastListDTO chatDto) {
        BroadcastList list = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Lista de transmissão com id %s não encontrada", id)));
        Chat chat = chatRepository.findById((chatDto.getChatId()))
                .orElseThrow(() -> new RuntimeException(String.format("Chat com id %s não encontrado", chatDto.getChatId())));
        list.getChats().add(chat);
        broadcastListRepository.save(list);
        return broadcastListMapper.toDTO(list);
    }

    public BroadcastListDTO removeChat(UUID id, AddChatToBroadcastListDTO chatDto) {
        BroadcastList list = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Lista de transmissão com id %s não encontrada", id)));
        Chat chat = chatRepository.findById((chatDto.getChatId()))
                .orElseThrow(() -> new RuntimeException(String.format("Chat com id %s não encontrado", chatDto.getChatId())));
        list.getChats().remove(chat);
        broadcastListRepository.save(list);
        return broadcastListMapper.toDTO(list);
    }

    public List<ChatDTO> getChatsFromList(UUID id) {
        BroadcastList list = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Lista de transmissão com id %s não encontrada", id)));

        return chatMapper.toDTOList(list.getChats());
    }
}
