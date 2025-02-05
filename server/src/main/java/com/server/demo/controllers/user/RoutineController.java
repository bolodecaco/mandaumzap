package com.server.demo.controllers.user;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.dtos.UpdateRoutineDTO;
import com.server.demo.services.JwtService;
import com.server.demo.services.RoutineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/routines")
@Tag(name = "Rotina", description = "APIs para gerenciamento de Rotinas")
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    @Autowired
    private JwtService jwtService;

    @Operation(summary = "Retorna todas as rotinas")
    @GetMapping
    public ResponseEntity<List<RoutineDTO>> getAllRoutines() {
        return ResponseEntity.ok(routineService.getAllRoutines(jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Retorna uma rotina pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<RoutineDTO> getRoutineById(@PathVariable UUID id) {
        return ResponseEntity.ok(routineService.getRoutineById(id, jwtService.getCurrentUserId()));
    }

    @Operation(summary = "Atualiza uma rotina pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<RoutineDTO> updateRoutine(@Valid @PathVariable UUID id, @RequestBody UpdateRoutineDTO routineDetails) {
        return ResponseEntity.ok(routineService.updateRoutine(id, routineDetails, jwtService.getCurrentUserId()));
    }


    @Operation(summary = "Cria uma nova rotina")
    @PostMapping
    public ResponseEntity<RoutineDTO> createRoutine(@Valid @RequestBody RequestRoutineDTO routine) {
        return ResponseEntity.ok(routineService.createRoutine(routine, jwtService.getCurrentUserId()));
    }

}
