package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.dtos.RequestBroadcastListDTO;
import com.server.demo.mappers.BroadcastListMapper;
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

    public List<BroadcastListDTO> findAllByUserId(UUID userId) {
        List<BroadcastList> list = broadcastListRepository.findAllByUserId(userId);
        return list.stream()
                .map(broadcastListMapper::toDTO)
                .toList();
    }

    public List<BroadcastListDTO> getAllLists() {
        return broadcastListRepository.findAll().stream()
                .map(broadcastListMapper::toDTO)
                .collect(Collectors.toList());

    }

    public BroadcastListDTO getListById(UUID id) {
        BroadcastList list = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BroadcastList not found"));
        return broadcastListMapper.toDTO(list);
    }

    public BroadcastListDTO createList(RequestBroadcastListDTO list) {
        BroadcastList currentList = broadcastListMapper.toEntity(list);
        broadcastListRepository.save(currentList);
        return broadcastListMapper.toDTO(currentList);
    }

    public BroadcastListDTO updateList(UUID id, BroadcastList listDetails) {
        BroadcastList existingList = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BroadcastList not found"));
        if (listDetails.getTitle() != null) {
            existingList.setTitle(listDetails.getTitle());
        }
        if (listDetails.getLastActiveAt() != null) {
            existingList.setLastActiveAt(listDetails.getLastActiveAt());
        }
        if (listDetails.getMessagesSent() != null) {
            existingList.setMessagesSent(listDetails.getMessagesSent());
        }
        if (listDetails.getChats() != null) {
            existingList.setChats(listDetails.getChats());
        }
        BroadcastList updatedList = broadcastListRepository.save(existingList);
        return broadcastListMapper.toDTO(updatedList);
    }

    public void deleteList(UUID id) {
        broadcastListRepository.deleteById(id);
    }

    public void addChat(UUID id, String chatId) {
        if (!isValidUUID(chatId)) {
            throw new IllegalArgumentException("Invalid UUID format");
        }
        BroadcastList list = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BroadcastList not found"));
        Chat chat = chatRepository.findById(UUID.fromString(chatId))
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        list.getChats().add(chat);
        broadcastListRepository.save(list);
    }

    private boolean isValidUUID(String chatId) {
        try {
            UUID.fromString(chatId);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public void removeChat(UUID id, String chatId) {
        BroadcastList list = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BroadcastList not found"));
        Chat chat = chatRepository.findById(UUID.fromString(chatId))
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        list.getChats().remove(chat);
        broadcastListRepository.save(list);
    }
}
