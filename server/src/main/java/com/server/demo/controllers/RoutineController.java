package com.server.demo.controllers;

import com.server.demo.dtos.RoutineDTO;
import com.server.demo.models.Routine;
import com.server.demo.services.RoutineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/routines")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }
    @GetMapping
    public ResponseEntity<List<RoutineDTO>> getAllRoutines() {
        List<RoutineDTO> routines = routineService.getAllRoutines();
        return ResponseEntity.ok(routines);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoutineDTO> getRoutineById(@PathVariable UUID id) {
        return routineService.getRoutineById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<RoutineDTO>> getRoutinesByOwnerId(@PathVariable UUID ownerId) {
        List<RoutineDTO> routines = routineService.getRoutinesByOwnerId(ownerId);
        return ResponseEntity.ok(routines);
    }

    @PostMapping
    public ResponseEntity<RoutineDTO> createRoutine(@RequestBody Routine routine) {
        
        RoutineDTO createdRoutine = routineService.createRoutine(routine);
        return ResponseEntity.ok(createdRoutine);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoutineDTO> updateRoutine(@PathVariable UUID id, @RequestBody Routine updatedRoutine) {
        RoutineDTO updatedRoutineDTO = routineService.updateRoutine(id, updatedRoutine);
        return ResponseEntity.ok(updatedRoutineDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable UUID id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.noContent().build();
    }
}
