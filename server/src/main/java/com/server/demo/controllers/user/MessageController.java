package com.server.demo.controllers.user;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.services.JwtService;
import com.server.demo.services.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/messages")
@Tag(name = "Mensagens", description = "API de mensagens")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Retorna todas as mensagens")
    @GetMapping
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        return ResponseEntity.ok(messageService.getActiveMessages(jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Retorna uma mensagem pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.getMessageById(id, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Retorna todas as mensagens de uma sessão")
    @GetMapping("/user/{sessionId}")
    public ResponseEntity<List<MessageDTO>> getMessagesBySessionId(@PathVariable UUID sessionId) {
        return ResponseEntity.ok(messageService.getMessagesBySessionId(sessionId, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Cria uma nova mensagem")
    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@Valid @RequestBody RequestMessageDTO message) {
        return ResponseEntity.ok(messageService.saveMessage(message, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Envia uma mensagem")
    @PostMapping("/{id}/send")
    public ResponseEntity<MessageDTO> sendMessage(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.sendMessage(id));
    }

    @Operation(summary = "Deleta uma mensagem")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id, jwtService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta todas as mensagens do usuário")
    @DeleteMapping
    public ResponseEntity<String> deleteAllMessages() {
        messageService.deleteAllByUSerId(jwtService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}
