package com.server.demo.controllers;

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
import com.server.demo.services.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Mensagens", description = "API de mensagens")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Operation(summary = "Retorna todas as mensagens")
    @GetMapping
    public ResponseEntity<List<MessageDTO>> getAllMessages() {
        List<MessageDTO> messages = (List<MessageDTO>) messageService.getActiveMessages();
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Retorna uma mensagem pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getMessageById(@PathVariable UUID id) {
        return ResponseEntity.ok(messageService.getMessageById(id));
    }

    @Operation(summary = "Retorna todas as mensagens de um usuário")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByUserId(@PathVariable UUID userId) {
        List<MessageDTO> messages = messageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "Cria uma nova mensagem")
    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@RequestBody RequestMessageDTO message) {
        MessageDTO createdMessage = messageService.saveMessage(message);
        return ResponseEntity.ok(createdMessage);
    }

    @Operation(summary = "Envia uma mensagem")
    @PostMapping("/{id}/send")
    public ResponseEntity<MessageDTO> sendMessage(@PathVariable UUID id, @RequestBody RequestMessageDTO requestMessage) {
        return ResponseEntity.ok(messageService.sendMessage(id, requestMessage));
    }

    @Operation(summary = "Deleta uma mensagem")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID id) {
        messageService.deleteMessage(id);
        return ResponseEntity.noContent().build();
    }
}
