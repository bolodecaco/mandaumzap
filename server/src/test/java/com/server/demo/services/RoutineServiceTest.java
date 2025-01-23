package com.server.demo.services;

import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.mappers.RoutineMapper;
import com.server.demo.models.Routine;
import com.server.demo.repositories.RoutineRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    @DisplayName("Criar rotina com dados válidos")
    void createRoutineWithValidData() {
        RequestRoutineDTO requestDTO = Instancio.create(RequestRoutineDTO.class);
        Routine routine = Instancio.create(Routine.class);
        RoutineDTO responseDTO = Instancio.create(RoutineDTO.class);

        when(routineMapper.toEntity(requestDTO)).thenReturn(routine);
        when(routineRepository.save(routine)).thenReturn(routine);
        when(routineMapper.toDTO(routine)).thenReturn(responseDTO);

        RoutineDTO data = routineService.createRoutine(requestDTO);

        assertEquals(responseDTO, data);
        verify(routineMapper).toEntity(requestDTO);
        verify(routineRepository).save(routine);
        verify(routineMapper).toDTO(routine);
    }

    @Test
    @DisplayName("Buscar rotina por ID com ID válido")
    void getRoutineByIdWithValidId() {
        UUID id = UUID.randomUUID();
        Routine routine = Instancio.create(Routine.class);
        RoutineDTO responseDTO = Instancio.create(RoutineDTO.class);

        when(routineRepository.findById(id)).thenReturn(Optional.of(routine));
        when(routineMapper.toDTO(routine)).thenReturn(responseDTO);

        RoutineDTO data = routineService.getRoutineById(id);

        assertEquals(responseDTO, data);
        verify(routineRepository).findById(id);
        verify(routineMapper).toDTO(routine);
    }

    @Test
    @DisplayName("Buscar rotinas por ID do dono")
    void getRoutinesByOwnerId() {
        UUID ownerId = UUID.randomUUID();
        List<Routine> routines = List.of(Instancio.create(Routine.class));
        List<RoutineDTO> responseDTOs = List.of(Instancio.create(RoutineDTO.class));

        when(routineRepository.findByOwnerId(ownerId)).thenReturn(routines);
        when(routineMapper.toDTOList(routines)).thenReturn(responseDTOs);

        List<RoutineDTO> data = routineService.getRoutinesByOwnerId(ownerId);

        assertEquals(responseDTOs, data);
        verify(routineRepository).findByOwnerId(ownerId);
        verify(routineMapper).toDTOList(routines);
    }

    @Test
    @DisplayName("Deletar rotina com ID válido")
    void deleteRoutineWithValidId() {
        UUID id = UUID.randomUUID();
        Routine routine = Instancio.create(Routine.class);

        when(routineRepository.findById(id)).thenReturn(Optional.of(routine));

        routineService.deleteRoutine(id);

        verify(routineRepository).deleteById(id);
    }

    @Test
    @DisplayName("Buscar todas as rotinas")
    void getAllRoutines() {
        List<Routine> routines = List.of(Instancio.create(Routine.class));
        List<RoutineDTO> responseDTOs = List.of(Instancio.create(RoutineDTO.class));

        when(routineRepository.findAll()).thenReturn(routines);
        when(routineMapper.toDTOList(routines)).thenReturn(responseDTOs);

        List<RoutineDTO> data = routineService.getAllRoutines();

        assertEquals(responseDTOs, data);
        verify(routineRepository).findAll();
        verify(routineMapper).toDTOList(routines);
    }

    @Test
    @DisplayName("Buscar rotina com ID inválido deve falhar")
    void getRoutineByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        
        when(routineRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> routineService.getRoutineById(id));
        verify(routineRepository).findById(id);
        verifyNoMoreInteractions(routineRepository);
        verifyNoInteractions(routineMapper);
    }
}