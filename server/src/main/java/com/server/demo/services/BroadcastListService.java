package com.server.demo.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.BroadcastListDTO;
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

    public List<BroadcastListDTO> getAllLists() {
        return broadcastListRepository.findAll().stream()
                .map(
                        chat -> new BroadcastListDTO(
                                chat.getId(),
                                chat.getOwner().getId(),
                                chat.getChats(),
                                chat.getTitle(),
                                chat.getLastActiveAt(),
                                chat.getMessagesSent()
                        )
                ).collect(Collectors.toList());
    }

    public BroadcastListDTO getListById(UUID id) {
        BroadcastList list = broadcastListRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("BroadcastList not found"));
        return new BroadcastListDTO(
                list.getId(),
                list.getOwner().getId(),
                list.getChats(),
                list.getTitle(),
                list.getLastActiveAt(),
                list.getMessagesSent()
        );
    }

    public BroadcastListDTO createList(BroadcastList list) {
        BroadcastList currentList = broadcastListRepository.save(list);
        return new BroadcastListDTO(
                currentList.getId(),
                currentList.getOwner().getId(),
                currentList.getChats(),
                currentList.getTitle(),
                currentList.getLastActiveAt(),
                currentList.getMessagesSent()
        );
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
        return new BroadcastListDTO(
                updatedList.getId(),
                updatedList.getOwner().getId(),
                updatedList.getChats(),
                updatedList.getTitle(),
                updatedList.getLastActiveAt(),
                updatedList.getMessagesSent()
        );
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
