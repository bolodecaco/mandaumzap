package com.server.demo.controllers;

import com.server.demo.dtos.RoutineDTO;
import com.server.demo.models.Routine;
import com.server.demo.services.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/routines")
@Tag(name = "Rotina", description = "APIs para gerenciamento de Rotinas")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @Operation(summary = "Buscar todas as rotinas", description = "Recupera uma lista com todas as rotinas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rotinas recuperadas com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<RoutineDTO>> getAllRoutines() {
        List<RoutineDTO> routines = routineService.getAllRoutines();
        return ResponseEntity.ok(routines);
    }

    @Operation(summary = "Buscar rotina por ID", description = "Recupera uma rotina específica pelo seu ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rotina encontrada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Rotina não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RoutineDTO> getRoutineById(
            @Parameter(description = "ID da rotina", required = true) 
            @PathVariable UUID id) {
        return routineService.getRoutineById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Buscar rotinas por ID do proprietário", description = "Recupera todas as rotinas pertencentes a um proprietário específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rotinas recuperadas com sucesso")
    })
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<RoutineDTO>> getRoutinesByOwnerId(
            @Parameter(description = "ID do proprietário", required = true)
            @PathVariable UUID ownerId) {
        List<RoutineDTO> routines = routineService.getRoutinesByOwnerId(ownerId);
        return ResponseEntity.ok(routines);
    }

    @Operation(summary = "Criar nova rotina", description = "Cria uma nova rotina")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rotina criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PostMapping
    public ResponseEntity<RoutineDTO> createRoutine(
            @Parameter(description = "Detalhes da rotina", required = true)
            @RequestBody Routine routine) {
        RoutineDTO createdRoutine = routineService.createRoutine(routine);
        return ResponseEntity.ok(createdRoutine);
    }

    @Operation(summary = "Atualizar rotina", description = "Atualiza uma rotina existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rotina atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Rotina não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RoutineDTO> updateRoutine(
            @Parameter(description = "ID da rotina a ser atualizada", required = true)
            @PathVariable UUID id,
            @Parameter(description = "Detalhes atualizados da rotina", required = true)
            @RequestBody Routine updatedRoutine) {
        RoutineDTO updatedRoutineDTO = routineService.updateRoutine(id, updatedRoutine);
        return ResponseEntity.ok(updatedRoutineDTO);
    }

    @Operation(summary = "Excluir rotina", description = "Remove uma rotina")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Rotina excluída com sucesso"),
        @ApiResponse(responseCode = "404", description = "Rotina não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(
            @Parameter(description = "ID da rotina a ser excluída", required = true)
            @PathVariable UUID id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }
}
