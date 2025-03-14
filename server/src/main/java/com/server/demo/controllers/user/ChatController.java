package com.server.demo.controllers.user;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
import com.server.demo.services.ChatService;
import com.server.demo.services.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/chats")
@Tag(name = "Chats", description = "API chats do usuário")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Retorna todos os chats")
    @GetMapping
    public ResponseEntity<Page<ChatDTO>> getAllChats(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String sessionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "chatName") String sort
    ) {
        String sortDirection = sort.startsWith("-") ? "DESC" : "ASC";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sort.replace("-", "")));
        return ResponseEntity.ok(chatService.getAllChats(jwtService.getCurrentUserId(), pageable, search, sessionId));
    }

    @Operation(summary = "Retorna um chat pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ChatDTO> getChatById(@PathVariable UUID id) {
        return ResponseEntity.ok(chatService.getChatDTOById(id, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Cria um novo chat")
    @PostMapping
    public ResponseEntity<ChatDTO> createChat(@Valid @RequestBody RequestChatDTO chat) {
        return ResponseEntity.ok(chatService.createChat(chat, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Atualiza um chat pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<ChatDTO> updateChat(@Valid @PathVariable UUID id, @RequestBody UpdateChatDTO chatDetails) {
        return ResponseEntity.ok(chatService.updateChat(id, chatDetails, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Deleta um chat pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChat(@PathVariable UUID id) {
        chatService.deleteChat(id, jwtService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

}
