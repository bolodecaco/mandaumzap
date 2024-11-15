package com.server.demo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.models.BroadcastList;
import com.server.demo.services.BroadcastListService;

@RestController
@RequestMapping("/api/lists")
public class BroadcastListController {

    @Autowired
    private BroadcastListService listService;

    @GetMapping
    public List<BroadcastListDTO> getAllLists() {
        return listService.getAllLists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BroadcastListDTO> getListById(@PathVariable UUID id) {
        return ResponseEntity.ok(listService.getListById(id));
    }

    @PostMapping
    public ResponseEntity<BroadcastListDTO> createList(@RequestBody BroadcastList list) {
        return ResponseEntity.ok(listService.createList(list));
    }

    @PostMapping("/{id}/chats")
    public ResponseEntity<String> addChatToList(@PathVariable UUID id, @RequestBody RequestChatDTO chat) {
        if (chat.getChatId() == null) {
            return ResponseEntity.badRequest().body("Chat ID is required");
        }
        listService.addChat(id, chat.getChatId());
        return ResponseEntity.ok("Chat added to list");
    }

    @DeleteMapping("/{id}/chats")
    public ResponseEntity<String> removeChatFromList(@PathVariable UUID id, @RequestBody RequestChatDTO chat) {
        if (chat.getChatId() == null) {
            return ResponseEntity.badRequest().body("Chat ID is required");
        }
        listService.removeChat(id, chat.getChatId());
        return ResponseEntity.ok("Chat removed from list");
    }

    @PutMapping("/{id}")
    public ResponseEntity<BroadcastListDTO> updateList(@PathVariable UUID id, @RequestBody BroadcastList listDetails) {
        return ResponseEntity.ok(listService.updateList(id, listDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable UUID id) {
        listService.deleteList(id);
        return ResponseEntity.noContent().build();
    }

}
