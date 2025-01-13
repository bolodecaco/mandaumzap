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

import com.server.demo.dtos.AddChatToBroadcastListDTO;
import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestBroadcastListDTO;
import com.server.demo.dtos.UpdateBroadcastListDTO;
import com.server.demo.services.BroadcastListService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/lists")
@Tag(name = "Lista de transmissão", description = "API das listas de transmissões")
public class BroadcastListController {

    @Autowired
    private BroadcastListService listService;

    @Operation(summary = "Retorna todas as listas de transmissão")
    @GetMapping
    public ResponseEntity<List<BroadcastListDTO>> getAllLists() {
        return ResponseEntity.ok(listService.getAllLists());
    }

    @Operation(summary = "Retorna uma lista de transmissão pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<BroadcastListDTO> getListById(@PathVariable UUID id) {
        return ResponseEntity.ok(listService.getListById(id));
    }

    @Operation(summary = "Cria uma nova lista de transmissão")
    @PostMapping
    public ResponseEntity<BroadcastListDTO> createList(@RequestBody RequestBroadcastListDTO list) {
        return ResponseEntity.ok(listService.createList(list));
    }

    @Operation(summary = "Adiciona um chat a uma lista de transmissão")
    @PostMapping("/{id}/chats")
    public ResponseEntity<BroadcastListDTO> addChatToList(@PathVariable UUID id, @RequestBody AddChatToBroadcastListDTO chatDto) {
        return ResponseEntity.ok(listService.addChat(id, chatDto));
    }

    @Operation(summary = "Remove um chat de uma lista de transmissão")
    @DeleteMapping("/{id}/chats")
    public ResponseEntity<BroadcastListDTO> removeChatFromList(@PathVariable UUID id, @RequestBody AddChatToBroadcastListDTO chatDto) {
        return ResponseEntity.ok(listService.removeChat(id, chatDto));
    }

    @Operation(summary = "Atualiza uma lista de transmissão")
    @PutMapping("/{id}")
    public ResponseEntity<BroadcastListDTO> updateList(@PathVariable UUID id, @RequestBody UpdateBroadcastListDTO listDetails) {
        return ResponseEntity.ok(listService.updateList(id, listDetails));
    }

    @Operation(summary = "Deleta uma lista de transmissão")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable UUID id) {
        listService.deleteList(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retorna todas as listas de transmissão de um usuário")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BroadcastListDTO>> findAllByUserId(@PathVariable UUID userId) {
        return ResponseEntity.ok(listService.findAllByUserId(userId));
    }

    @Operation(summary = "Retorna todos os chats de uma lista de transmissão")
    @GetMapping("/{id}/chats")
    public ResponseEntity<List<ChatDTO>> getChatsFromList(@PathVariable UUID id) {
        return ResponseEntity.ok(listService.getChatsFromList(id));
    }

}
