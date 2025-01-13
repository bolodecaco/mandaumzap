package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.mappers.RoutineMapper;
import com.server.demo.models.Routine;
import com.server.demo.repositories.RoutineRepository;

@Service
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private RoutineMapper routineMapper;

    public RoutineService(RoutineRepository routineRepository, RoutineMapper routineMapper) {
        this.routineRepository = routineRepository;
        this.routineMapper = routineMapper;
    }

    public List<RoutineDTO> getAllRoutines() {
        List<Routine> routines = routineRepository.findAll();
        return routineMapper.toDTOList(routines);
    }

    public RoutineDTO getRoutineById(UUID id) {
        Routine routine = routineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Rotina com ID %s não encontrada", id)));
        return routineMapper.toDTO(routine);
    }

    public List<RoutineDTO> getRoutinesByOwnerId(UUID ownerId) {
        List<Routine> routines = routineRepository.findByOwnerId(ownerId);
        return routineMapper.toDTOList(routines);
    }

    public RoutineDTO createRoutine(RequestRoutineDTO routine) {
        Routine newRoutine = routineMapper.toEntity(routine);
        routineRepository.save(newRoutine);
        return routineMapper.toDTO(newRoutine);
    }

    public RoutineDTO updateRoutine(UUID id, Routine updatedRoutine) {
        return routineRepository.findById(id).map(routine -> {
            routine.setTitle(updatedRoutine.getTitle());
            routine.setMessage(updatedRoutine.getMessage());
            routine.setWillActiveAt(updatedRoutine.getWillActiveAt());
            routine.setLastActiveAt(updatedRoutine.getLastActiveAt());
            routine.setTimesSent(updatedRoutine.getTimesSent());
            Routine savedRoutine = routineRepository.save(routine);
            return routineMapper.toDTO(savedRoutine);
        }).orElseThrow(() -> new RuntimeException(String.format("Rotina com ID %s não encontrada", id)));
    }

    public void deleteRoutine(UUID id) {
        routineRepository.deleteById(id);
    }
}
