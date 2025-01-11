package com.server.demo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.RequestSessionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
import com.server.demo.services.SessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/sessions")
@Tag(name = "Sessões", description = "API de sessões")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Operation(summary = "Retorna todas as sessões")
    @GetMapping
    public List<SessionDTO> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @Operation(summary = "Retorna uma sessão pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @Operation(summary = "Cria uma nova sessão")
    @PostMapping
    public ResponseEntity<SessionDTO> createSession(@RequestBody RequestSessionDTO session) {
        return ResponseEntity.ok(sessionService.createSession(session));
    }

    @Operation(summary = "Atualiza a atividade de uma sessão")
    @PatchMapping("/{id}")
    public ResponseEntity<SessionDTO> updateSessionActivity(@PathVariable UUID id, @RequestBody UpdateSessionDTO updateSessionDTO) {
        return ResponseEntity.ok(sessionService.updateSessionActivity(id, updateSessionDTO));
    }

    @Operation(summary = "Retorna todas as sessões de um usuário")
    @GetMapping("/user/{userId}")
    public List<SessionDTO> getUserSessions(@PathVariable UUID userId) {
        return sessionService.getUserSessions(userId);
    }

    @Operation(summary = "Deleta uma sessão")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID id) {
        sessionService.deleteSession(id);
        return ResponseEntity.noContent().build();
    }
}
