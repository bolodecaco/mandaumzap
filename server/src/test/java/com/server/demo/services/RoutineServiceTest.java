package com.server.demo.services;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.mappers.RoutineMapper;
import com.server.demo.models.Routine;
import com.server.demo.repositories.RoutineRepository;

class RoutineServiceTest {

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private RoutineMapper routineMapper;

    @InjectMocks
    private RoutineService routineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create rotine")
    void createRoutine() {
        RequestRoutineDTO requestRoutineDTO = new RequestRoutineDTO();
        requestRoutineDTO.setTitle("Minha rotina");
        requestRoutineDTO.setWillActiveAt(new Date());
        Routine message = new Routine();
        when(routineMapper.toEntity(requestRoutineDTO)).thenReturn(message);
        routineService.createRoutine(requestRoutineDTO);
        assertNotNull(message, "Mensagem não pode ser nula");
    }

    @Test
    @DisplayName("Get routine by id")
    void getRoutineById() {
        UUID id = UUID.randomUUID();
        when(routineRepository.findById(id)).thenReturn(java.util.Optional.of(new Routine()));
        routineService.getRoutineById(id);
        verify(routineRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Get rotines by owner id")
    void getRoutinesByOwnerId() {
        UUID ownerId = UUID.randomUUID();
        Routine routine = new Routine();
        when(routineRepository.findByOwnerId(ownerId)).thenReturn(java.util.List.of(routine));
        routineService.getRoutinesByOwnerId(ownerId);
        verify(routineRepository, times(1)).findByOwnerId(ownerId);
    }

    @Test
    @DisplayName("Update routine")
    void updateRoutine() {
        UUID id = UUID.randomUUID();
        Routine existingRoutine = new Routine();
        existingRoutine.setId(id);
        existingRoutine.setTitle("Old Title");
        Routine updatedRoutine = new Routine();
        updatedRoutine.setTitle("New Title");
        RoutineDTO routineDTO = new RoutineDTO();
        routineDTO.setTitle("New Title");
        when(routineRepository.findById(id)).thenReturn(java.util.Optional.of(existingRoutine));
        when(routineRepository.save(existingRoutine)).thenReturn(existingRoutine);
        when(routineMapper.toDTO(existingRoutine)).thenReturn(routineDTO);
        routineService.updateRoutine(id, updatedRoutine);
        verify(routineRepository).findById(id);
        verify(routineRepository).save(existingRoutine);
        verify(routineMapper).toDTO(existingRoutine);
    }

    @Test
    @DisplayName("Delete routine")
    void deleteRoutine() {
        UUID id = UUID.randomUUID();
        Routine routine = new Routine();
        when(routineRepository.findById(id)).thenReturn(java.util.Optional.of(routine));
        routineService.deleteRoutine(id);
        verify(routineRepository, times(1)).deleteById(id);
    }
}
