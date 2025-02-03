package com.server.demo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.scheduler.DynamicScheduler;
import com.server.demo.services.RoutineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/routines")
@Tag(name = "Rotina", description = "APIs para gerenciamento de Rotinas")
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    @Autowired
    private DynamicScheduler dynamicScheduler;

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
    public ResponseEntity<RoutineDTO> createRoutine(@Valid @RequestBody RequestRoutineDTO routine) {
        RoutineDTO service = routineService.createRoutine(routine);
        dynamicScheduler.updateJobs();
        return ResponseEntity.ok(service);
    }

}
