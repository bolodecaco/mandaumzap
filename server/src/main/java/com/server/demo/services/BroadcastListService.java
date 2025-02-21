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
import com.server.demo.exception.BusinessException;
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

    public List<BroadcastListDTO> findAllByUserId(String userId) {
        List<BroadcastList> list = broadcastListRepository.findAllByUserId(userId);
        return broadcastListMapper.toDTOList(list);
    }

    public BroadcastListDTO getListById(UUID id, String userId) {
        BroadcastList list = broadcastListRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException("Lista de transmissão não encontrada ou acesso negado"));
        return broadcastListMapper.toDTO(list);
    }

    public BroadcastListDTO createList(RequestBroadcastListDTO list, String userId) {
        BroadcastList currentList = broadcastListMapper.toEntity(list);
        currentList.setUserId(userId);
        broadcastListRepository.save(currentList);
        return broadcastListMapper.toDTO(currentList);
    }

    public BroadcastListDTO updateList(UUID id, UpdateBroadcastListDTO listDetails, String userId) {
        BroadcastList existingList = broadcastListRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Lista de transmissão com id %s não encontrada", id)));
        broadcastListMapper.updateEntityFromDTO(listDetails, existingList);
        broadcastListRepository.save(existingList);
        return broadcastListMapper.toDTO(existingList);
    }

    public void incrementMessageSent(UUID id, String userId) {
        BroadcastList list = broadcastListRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Lista de transmissão com id %s não encontrada", id)));
        list.setMessagesSent(list.getMessagesSent() + 1);
        broadcastListRepository.save(list);
    }

    public void deleteList(UUID id, String userId) {
        BroadcastList list = broadcastListRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Lista de transmissão com id %s não encontrada", id)));
        broadcastListRepository.delete(list);
    }

    public BroadcastListDTO addChats(UUID id, List<AddChatToBroadcastListDTO> chatsDto, String userId) {
        BroadcastList list = broadcastListRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Lista de transmissão com id %s não encontrada", id)));

        List<Chat> chats = chatsDto.stream()
                .map(chatDto -> chatRepository.findById(chatDto.getChatId())
                .orElseThrow(() -> new BusinessException(String.format("Chat com id %s não encontrado", chatDto.getChatId()))))
                .toList();

        list.getChats().addAll(chats);
        broadcastListRepository.save(list);

        return broadcastListMapper.toDTO(list);
    }

    public BroadcastListDTO removeChat(UUID id, AddChatToBroadcastListDTO chatDto, String userId) {
        BroadcastList list = broadcastListRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Lista de transmissão com id %s não encontrada", id)));
        Chat chat = chatRepository.findById((chatDto.getChatId()))
                .orElseThrow(() -> new BusinessException(String.format("Chat com id %s não encontrado", chatDto.getChatId())));
        list.getChats().remove(chat);
        broadcastListRepository.save(list);
        return broadcastListMapper.toDTO(list);
    }

    public List<ChatDTO> getChatsFromList(UUID id, String userId) {
        BroadcastList list = broadcastListRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Lista de transmissão com id %s não encontrada", id)));
        return chatMapper.toDTOList(list.getChats());
    }
}
