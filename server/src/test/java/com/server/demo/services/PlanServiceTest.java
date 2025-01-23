package com.server.demo.services;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.dtos.RequestPlanDTO;
import com.server.demo.models.Plan;
import com.server.demo.mappers.PlanMapper;
import com.server.demo.repositories.PlanRepository;
import com.server.demo.enums.PlanType;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PlanMapper planMapper;

    @InjectMocks
    private PlanService planService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Criar plano com dados válidos")
    void createPlanWithValidData() {
        RequestPlanDTO requestDTO = Instancio.create(RequestPlanDTO.class);
        Plan plan = Instancio.create(Plan.class);
        PlanDTO responseDTO = Instancio.create(PlanDTO.class);

        when(planMapper.toEntityByRequestPlan(requestDTO)).thenReturn(plan);
        when(planRepository.save(plan)).thenReturn(plan);
        when(planMapper.toDTO(plan)).thenReturn(responseDTO);

        PlanDTO data = planService.createPlan(requestDTO);

        assertEquals(responseDTO, data);
        verify(planMapper).toEntityByRequestPlan(requestDTO);
        verify(planRepository).save(plan);
        verify(planMapper).toDTO(plan);
    }

    @Test
    @DisplayName("Buscar plano por tipo com tipo válido")
    void getPlanByTypeWithValidType() {
        String type = "FREE";
        Plan plan = Instancio.create(Plan.class);
        PlanDTO responseDTO = Instancio.create(PlanDTO.class);

        when(planRepository.findByType(PlanType.FREE)).thenReturn(Optional.of(plan));
        when(planMapper.toDTO(plan)).thenReturn(responseDTO);

        PlanDTO data = planService.getPlanByType(type);

        assertEquals(responseDTO, data);
        verify(planRepository).findByType(PlanType.FREE);
        verify(planMapper).toDTO(plan);
    }

    @Test
    @DisplayName("Buscar plano por tipo com tipo inválido deve falhar")
    void getPlanByTypeWithInvalidType() {
        String invalidType = "INVALID_TYPE";

        assertThrows(RuntimeException.class, () -> planService.getPlanByType(invalidType));
    }

    @Test
    @DisplayName("Atualizar plano com dados válidos")
    void updatePlanWithValidData() {
        UUID id = UUID.randomUUID();
        Plan existingPlan = Instancio.create(Plan.class);
        Plan updateDetails = Instancio.create(Plan.class);
        PlanDTO responseDTO = Instancio.create(PlanDTO.class);

        when(planRepository.findById(id)).thenReturn(Optional.of(existingPlan));
        when(planRepository.save(existingPlan)).thenReturn(existingPlan);
        when(planMapper.toDTO(existingPlan)).thenReturn(responseDTO);

        PlanDTO data = planService.updatePlan(id, updateDetails);

        assertEquals(responseDTO, data);
        verify(planRepository).findById(id);
        verify(planRepository).save(existingPlan);
        verify(planMapper).toDTO(existingPlan);
    }

    @Test
    @DisplayName("Deletar plano com ID válido")
    void deletePlanWithValidId() {
        UUID id = UUID.randomUUID();
        doNothing().when(planRepository).deleteById(id);

        planService.deletePlan(id);

        verify(planRepository).deleteById(id);
    }
}
