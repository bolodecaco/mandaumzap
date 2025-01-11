package com.server.demo.controllers;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.models.Routine;
import com.server.demo.services.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/routines")
@Tag(name = "Rotina", description = "APIs para gerenciamento de Rotinas")
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    @Operation(summary = "Retorna todas as rotinas")
    @GetMapping
    public ResponseEntity<List<RoutineDTO>> getAllRoutines() {
        return ResponseEntity.ok(routineService.getAllRoutines());
    }

    @Operation(summary = "Retorna uma rotina pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoutineDTO> getRoutineById(@PathVariable UUID id) {
        return ResponseEntity.ok(routineService.getRoutineById(id));
    }

    @Operation(summary = "Cria uma nova rotina")
    @PostMapping
    public ResponseEntity<RoutineDTO> createRoutine(@RequestBody RequestRoutineDTO routine) {
        return ResponseEntity.ok(routineService.createRoutine(routine));
    }

}
