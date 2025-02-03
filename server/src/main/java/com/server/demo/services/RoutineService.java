package com.server.demo.services;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.dtos.UpdateRoutineCronDTO;
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
        String cronExpression = getCronExpression(routine);
        Routine newRoutine = routineMapper.toEntity(routine);
        newRoutine.setCron(cronExpression);
        routineRepository.save(newRoutine);
        return routineMapper.toDTO(newRoutine);
    }

    public RoutineDTO updateRoutineCron(UpdateRoutineCronDTO updateRoutineDTO) {
        Routine routine = routineRepository.findById(updateRoutineDTO.getRoutineId()).orElseThrow(() -> new BusinessException(String.format("Rotina com ID %s não encontrada", updateRoutineDTO.getRoutineId())));
        routine.setCron(updateRoutineDTO.getCron());
        Routine savedRoutine = routineRepository.save(routine);
        return routineMapper.toDTO(savedRoutine);
    }

    public RoutineDTO updateRoutine(UUID id, Routine updatedRoutine) {
        Routine routine = routineRepository.findById(id).orElseThrow(() -> new BusinessException(String.format("Rotina com ID %s não encontrada", id)));
        routine.setTitle(updatedRoutine.getTitle());
        routine.setMessage(updatedRoutine.getMessage());
        routine.setLastActiveAt(updatedRoutine.getLastActiveAt());
        routine.setTimesSent(updatedRoutine.getTimesSent());
        Routine savedRoutine = routineRepository.save(routine);
        return routineMapper.toDTO(savedRoutine);
    }

    public void deleteRoutine(UUID id) {
        routineRepository.deleteById(id);
    }

    private String getCronExpression(RequestRoutineDTO routine) {
        LocalTime time = routine.getExecutionDateTime().toLocalTime();
        int minute = time.getMinute();
        int hour = time.getHour();
        switch (routine.getFrequency()) {
            case DAILY:
                return String.format("%d %d * * *", minute, hour);
            case WEEKLY:
                if (routine.getDaysOfWeek() == null || routine.getDaysOfWeek().isEmpty()) {
                    throw new BusinessException("Para frequência semanal, os dias da semana devem ser informados!");
                }
                return String.format("%d %d * * %s", minute, hour, routine.getDaysOfWeek());
            case MONTHLY:
                if (routine.getDayOfMonth() == null || routine.getDayOfMonth() < 1 || routine.getDayOfMonth() > 31) {
                    throw new BusinessException("Para frequência mensal, um dia válido do mês deve ser informado!");
                }
                return String.format("%d %d %d * *", minute, hour, routine.getDayOfMonth());
            default:
                throw new BusinessException("Frequência inválida!");
        }
    }
}
