package com.server.demo.services;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.dtos.UpdateRoutineCronDTO;
import com.server.demo.dtos.UpdateRoutineDTO;
import com.server.demo.enums.FrequencyType;
import com.server.demo.mappers.RoutineMapper;
import com.server.demo.models.Routine;
import com.server.demo.repositories.RoutineRepository;

@ExtendWith(MockitoExtension.class)
class RoutineServiceTest {

    @Mock
    private RoutineRepository routineRepository;

    @Mock
    private RoutineMapper routineMapper;

    @InjectMocks
    private RoutineService routineService;

    private Routine routine;
    private RoutineDTO routineDTO;
    private RequestRoutineDTO requestRoutineDTO;
    private UpdateRoutineDTO updateRoutineDTO;
    private UpdateRoutineCronDTO updateRoutineCronDTO;
    private String userId;
    private String routineName;
    private String cron;
    private Date lastActiveAt;
    private UUID messageId;
    private String executionTime;

    @BeforeEach
    void setUp() {
        userId = "testUserId";
        routineName = "testTitle";
        cron = "0 0 0 1 1 ? 2099";
        lastActiveAt = new Date();
        messageId = UUID.randomUUID();
        executionTime = "10:00";

        routine = new Routine();
        routine.setId(UUID.randomUUID());
        routine.setCron(cron);
        routine.setUserId(userId);
        routine.setLastActiveAt(lastActiveAt);
        routine.setTitle(routineName);

        routineDTO = new RoutineDTO();
        routineDTO.setCron(cron);
        routineDTO.setTitle(routineName);
        routineDTO.setMessageId(messageId);
        routineDTO.setLastActiveAt(lastActiveAt);
        routineDTO.setTimesSent(1);

        requestRoutineDTO = new RequestRoutineDTO();
        requestRoutineDTO.setFrequency(FrequencyType.DAILY);
        requestRoutineDTO.setTitle(routineName);
        requestRoutineDTO.setMessageId(messageId);
        requestRoutineDTO.setExecutionDateTime(executionTime);

        updateRoutineCronDTO = new UpdateRoutineCronDTO();
        updateRoutineCronDTO.setRoutineId(routine.getId());
        updateRoutineCronDTO.setCron(cron);
    }

    @Test
    void shouldCreateRoutine() {
        when(routineMapper.toEntity(requestRoutineDTO)).thenReturn(routine);
        when(routineRepository.save(routine)).thenReturn(routine);
        when(routineMapper.toDTO(routine)).thenReturn(routineDTO);

        RoutineDTO createdRoutine = routineService.createRoutine(requestRoutineDTO, userId);

        assertNotNull(createdRoutine);
        assertEquals(routineDTO, createdRoutine);
    }

    @Test
    void shouldGetAllRoutines() {
        when(routineRepository.findAll()).thenReturn(List.of(routine));
        when(routineMapper.toDTOList(List.of(routine))).thenReturn(List.of(routineDTO));

        List<RoutineDTO> routines = routineService.getAllRoutines();

        assertNotNull(routines);
        assertEquals(List.of(routineDTO), routines);
    }

    @Test
    void shouldGetRoutineById() {
        when(routineRepository.findByIdAndUserId(routine.getId(), userId)).thenReturn(java.util.Optional.of(routine));
        when(routineMapper.toDTO(routine)).thenReturn(routineDTO);

        RoutineDTO foundRoutine = routineService.getRoutineById(routine.getId(), userId);

        assertNotNull(foundRoutine);
        assertEquals(routineDTO, foundRoutine);
    }

    @Test
    void shouldUpdateTimesSent() {
        int timesSent = 2;
        when(routineRepository.findById(routine.getId())).thenReturn(java.util.Optional.of(routine));

        routineService.updateTimesSent(routine.getId(), timesSent);

        assertEquals(timesSent, routine.getTimesSent());
    }

    @Test
    void shouldUpdateRoutineCron() {
        String newCron = "0 0 0 1 1 ? 2099";
        when(routineRepository.findById(routine.getId())).thenReturn(java.util.Optional.of(routine));
        when(routineRepository.save(routine)).thenReturn(routine);
        when(routineMapper.toDTO(routine)).thenReturn(routineDTO);

        RoutineDTO updatedRoutine = routineService.updateRoutineCron(updateRoutineCronDTO);

        assertNotNull(updatedRoutine);
        assertEquals(routineDTO, updatedRoutine);
    }

    @Test
    void shouldupdateRoutine() {
        String newTitle = "newTitle";
        when(routineRepository.findByIdAndUserId(routine.getId(), userId)).thenReturn(java.util.Optional.of(routine));
        when(routineRepository.save(routine)).thenReturn(routine);
        when(routineMapper.toDTO(routine)).thenReturn(routineDTO);

        updateRoutineDTO = new UpdateRoutineDTO();
        updateRoutineDTO.setTitle(newTitle);

        RoutineDTO updatedRoutine = routineService.updateRoutine(routine.getId(), updateRoutineDTO, userId);

        assertNotNull(updatedRoutine);
        assertEquals(routineDTO, updatedRoutine);
    }

    @Test
    void shouldDeleteRoutine() {
        routineService.deleteRoutine(routine.getId());
    }


}
