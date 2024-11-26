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

import com.server.demo.dtos.ChatDTO;
import com.server.demo.models.Chat;
import com.server.demo.services.ChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/chats")
@Tag(name = "Chats", description = "API chats do usuário")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Operation(summary = "Retorna todos os chats")
    @GetMapping
    public List<ChatDTO> getAllChats() {
        return chatService.getAllChats();
    }

    @Operation(summary = "Retorna um chat pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable UUID id) {
        return ResponseEntity.ok(chatService.getChatDTOById(id));
    }

    @Operation(summary = "Cria um novo chat")
    @PostMapping
    public ResponseEntity<ChatDTO> createChat(@RequestBody Chat chat) {
        return ResponseEntity.ok(chatService.createChat(chat));
    }

    @Operation(summary = "Atualiza um chat pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<ChatDTO> updateChat(@PathVariable UUID id, @RequestBody Chat chatDetails) {
        return ResponseEntity.ok(chatService.updateChat(id, chatDetails));
    }

    @Operation(summary = "Deleta um chat pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable UUID id) {
        chatService.deleteChat(id);
        return ResponseEntity.noContent().build();
    }

}
