package com.server.demo.controllers.user;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.BotConnectionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
import com.server.demo.services.JwtService;
import com.server.demo.services.SessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/sessions")
@Tag(name = "Sessões", description = "API de sessões")
public class SessionController {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Retorna todas as sessões")
    @GetMapping
    public List<SessionDTO> getAllSessions() {
        return sessionService.getAllSessions(jwtService.getCurrentUserId());
    }

    @Operation(summary = "Retorna uma sessão pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> getSessionById(@PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.getSessionById(id, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Cria uma nova sessão")
    @PostMapping
    public ResponseEntity<BotConnectionDTO> createSession() {
        return ResponseEntity.ok(sessionService.createSession(jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Inicia uma sessão existente")
    @PatchMapping("/{id}/start")
    public ResponseEntity<BotConnectionDTO> startSession(@PathVariable UUID id) {
        return ResponseEntity.ok(sessionService.startSession(id, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Atualiza a atividade de uma sessão")
    @PatchMapping("/{id}")
    public ResponseEntity<SessionDTO> updateSessionActivity(@PathVariable UUID id, @Valid @RequestBody UpdateSessionDTO updateSessionDTO) {
        return ResponseEntity.ok(sessionService.updateSessionActivity(id, updateSessionDTO, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Deleta uma sessão")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSession(@PathVariable UUID id) {
        sessionService.deleteSession(id, jwtService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Deleta todas as sessões do usuário")
    @DeleteMapping("/")
    public ResponseEntity<Void> deleteAllSessions() {
        sessionService.deleteAllSessions(jwtService.getCurrentUserId());
        return ResponseEntity.noContent().build();
    }
}
