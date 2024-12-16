package com.server.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.mappers.MessageMapper;
import com.server.demo.mappers.RoutineMapper;
import com.server.demo.models.Message;
import com.server.demo.models.Routine;
import com.server.demo.models.User;
import com.server.demo.repositories.RoutineRepository;
import com.server.demo.repositories.UserRepository;

@Service
public class RoutineService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageMapper messageMapper;

    private final RoutineRepository routineRepository;
    private final RoutineMapper routineMapper;

    public RoutineService(RoutineRepository routineRepository, RoutineMapper routineMapper) {
        this.routineRepository = routineRepository;
        this.routineMapper = routineMapper;
    }

    public List<RoutineDTO> getAllRoutines() {
        List<Routine> routines = routineRepository.findAll();
        return routineMapper.toDTOList(routines);
    }

    public Optional<RoutineDTO> getRoutineById(UUID id) {
        return routineRepository.findById(id)
                .map(routineMapper::toDTO);
    }

    public List<RoutineDTO> getRoutinesByOwnerId(UUID ownerId) {
        List<Routine> routines = routineRepository.findByOwnerId(ownerId);
        return routineMapper.toDTOList(routines);
    }

    public RoutineDTO createRoutine(Routine routine) {
        User owner = userRepository.findById(routine.getOwner().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        routine.setOwner(owner);

        MessageDTO messageDTO = messageService.getMessageById(routine.getMessage().getId())
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        RequestMessageDTO requestMessageDTO = messageMapper.toRequestDTO(messageDTO);
        Message message = messageMapper.toEntity(requestMessageDTO);
        routine.setMessage(message);

        Routine savedRoutine = routineRepository.save(routine);
        return routineMapper.toDTO(savedRoutine);
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
        }).orElseThrow(() -> new RuntimeException("Routine not found"));
    }

    public void deleteRoutine(UUID id) {
        routineRepository.deleteById(id);
    }
}
