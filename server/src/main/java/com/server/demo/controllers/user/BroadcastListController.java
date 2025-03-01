package com.server.demo.controllers.user;

import java.util.List;
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

import com.server.demo.dtos.AddChatToBroadcastListDTO;
import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestBroadcastListDTO;
import com.server.demo.dtos.UpdateBroadcastListDTO;
import com.server.demo.services.BroadcastListService;
import com.server.demo.services.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/lists")
@Tag(name = "Lista de transmissão", description = "API das listas de transmissões")
public class BroadcastListController {

    @Autowired
    private BroadcastListService listService;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Retorna todos as listas")
    @GetMapping
    public ResponseEntity<Page<BroadcastListDTO>> getAllChats(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastActiveAt") String sort
    ) {
        String sortDirection = sort.startsWith("-") ? "DESC" : "ASC";
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDirection), sort.replace("-", "")));
        return ResponseEntity.ok(listService.findAllByUserId(jwtService.getCurrentUserId(), pageable, search));
    }

    @Operation(summary = "Retorna uma lista de transmissão pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<BroadcastListDTO> getListById(@PathVariable UUID id) {
        return ResponseEntity.ok(listService.getListById(id, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Cria uma nova lista de transmissão")
    @PostMapping
    public ResponseEntity<BroadcastListDTO> createList(@Valid @RequestBody RequestBroadcastListDTO list) {
        return ResponseEntity.ok(listService.createList(list, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Adiciona uma lista de chats a uma lista de transmissão")
    @PostMapping("/{id}/chats")
    public ResponseEntity<BroadcastListDTO> addChatToList(@PathVariable UUID id, @RequestBody List<AddChatToBroadcastListDTO> chatsDto) {
        return ResponseEntity.ok(listService.addChats(id, chatsDto, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Remove um chat de uma lista de transmissão")
    @DeleteMapping("/{id}/chats")
    public ResponseEntity<BroadcastListDTO> removeChatFromList(@PathVariable UUID id, @RequestBody AddChatToBroadcastListDTO chatDto) {
        return ResponseEntity.ok(listService.removeChat(id, chatDto, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Atualiza uma lista de transmissão")
    @PutMapping("/{id}")
    public ResponseEntity<BroadcastListDTO> updateList(@Valid @PathVariable UUID id, @RequestBody UpdateBroadcastListDTO listDetails) {
        return ResponseEntity.ok(listService.updateList(id, listDetails, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Deleta uma lista de transmissão")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(@PathVariable UUID id) {
        listService.deleteList(id, jwtService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Retorna todos os chats de uma lista de transmissão")
    @GetMapping("/{id}/chats")
    public ResponseEntity<List<ChatDTO>> getChatsFromList(@PathVariable UUID id) {
        return ResponseEntity.ok(listService.getChatsFromList(id, jwtService.getCurrentUserId()));
    }

}
