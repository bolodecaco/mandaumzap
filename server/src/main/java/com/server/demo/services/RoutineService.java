package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.dtos.UpdateRoutineDTO;
import com.server.demo.exception.BusinessException;
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

    public List<RoutineDTO> getAllRoutines(String userId) {
        List<Routine> routines = routineRepository.findAllByUserId(userId);
        return routineMapper.toDTOList(routines);
    }

    public RoutineDTO getRoutineById(UUID id, String userId) {
        Routine routine = routineRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new BusinessException(String.format("Rotina com ID %s não encontrada", id)));
        return routineMapper.toDTO(routine);
    }

    public RoutineDTO createRoutine(RequestRoutineDTO routine, String userId) {
        Routine newRoutine = routineMapper.toEntity(routine);
        newRoutine.setUserId(userId);
        routineRepository.save(newRoutine);
        return routineMapper.toDTO(newRoutine);
    }


    public RoutineDTO updateRoutine(UUID id, UpdateRoutineDTO updatedRoutine, String userId) {
        Routine existingRoutine = routineRepository.findByIdAndUserId(id, userId)
            .orElseThrow(() -> new BusinessException(String.format("Rotina com ID %s não encontrada", id)));
        routineMapper.updateEntityFromDTO(updatedRoutine, existingRoutine);
        routineRepository.save(existingRoutine);
        return routineMapper.toDTO(existingRoutine);
    }

    public void deleteRoutine(UUID id) {
        routineRepository.deleteById(id);
    }
}