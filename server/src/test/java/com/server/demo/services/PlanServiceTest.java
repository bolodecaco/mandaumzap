package com.server.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.instancio.Instancio;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.dtos.RequestPlanDTO;
import com.server.demo.dtos.UpdatePlanDTO;
import com.server.demo.enums.PlanType;
import com.server.demo.mappers.PlanMapper;
import com.server.demo.models.Plan;
import com.server.demo.repositories.PlanRepository;

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
    @DisplayName("Listar planos válidos")
    void listAllPlans() {
        Plan plan = Instancio.create(Plan.class);
        List<Plan> plans = List.of(plan);
        List<PlanDTO> responseDTOs = List.of(Instancio.create(PlanDTO.class));

        when(planRepository.findAll()).thenReturn(plans);
        when(planMapper.toDTOList(plans)).thenReturn(responseDTOs);

        List<PlanDTO> result = planService.getAllPlans();

        assertEquals(1, result.size());
        verify(planRepository).findAll();
        verify(planMapper).toDTOList(plans);
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
        UpdatePlanDTO updateDetails = Instancio.create(UpdatePlanDTO.class);
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
    @DisplayName("Atualizar plano com ID inválido deve falhar")
    void updatePlanWithInvalidId() {
        UUID id = UUID.randomUUID();
        UpdatePlanDTO updateDetails = Instancio.create(UpdatePlanDTO.class);

        when(planRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> planService.updatePlan(id, updateDetails));
        verify(planRepository).findById(id);
        verifyNoMoreInteractions(planRepository);
        verifyNoInteractions(planMapper);
    }

    @Test
    @DisplayName("Deletar plano com ID válido")
    void deletePlanWithValidId() {
        UUID id = UUID.randomUUID();
        doNothing().when(planRepository).deleteById(id);

        planService.deletePlan(id);

        verify(planRepository).deleteById(id);
    }

    @Test
    @DisplayName("Buscar plano por ID com ID válido")
    void getPlanByIdWithValidId() {
        UUID id = UUID.randomUUID();
        Plan plan = Instancio.create(Plan.class);
        PlanDTO expectedDTO = Instancio.create(PlanDTO.class);

        when(planRepository.findById(id)).thenReturn(Optional.of(plan));
        when(planMapper.toDTO(plan)).thenReturn(expectedDTO);

        PlanDTO result = planService.getPlanById(id);

        assertEquals(expectedDTO, result);
        verify(planRepository).findById(id);
        verify(planMapper).toDTO(plan);
    }

    @Test
    @DisplayName("Buscar plano por ID com ID inválido deve falhar")
    void getPlanByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        
        when(planRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> planService.getPlanById(id));
        verify(planRepository).findById(id);
        verifyNoInteractions(planMapper);
    }
}