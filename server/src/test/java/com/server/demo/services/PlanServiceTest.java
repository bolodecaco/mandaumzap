package com.server.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.enums.PlanType;
import com.server.demo.mappers.PlanMapper;
import com.server.demo.models.Plan;
import com.server.demo.repositories.PlanRepository;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private PlanMapper planMapper;

    @InjectMocks
    private PlanService planService;

    private Plan plan;
    private PlanDTO planDTO;

    @BeforeEach
    void setUp() {
        plan = new Plan();
        plan.setId(UUID.randomUUID());
        plan.setName("Plano Teste");
        plan.setType(PlanType.PREMIUM);
        plan.setPrice(99.9);
        plan.setBenefits("Todos os benefícios");

        planDTO = new PlanDTO();
        planDTO.setId(plan.getId());
        planDTO.setName(plan.getName());
        planDTO.setType(plan.getType());
        planDTO.setPrice(plan.getPrice());
        planDTO.setBenefits(plan.getBenefits());
    }

    @Test
    void shouldGetAllPlans() {
        when(planRepository.findAll()).thenReturn(List.of(plan));
        when(planMapper.toDTOList(any())).thenReturn(List.of(planDTO));

        List<PlanDTO> result = planService.getAllPlans();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(planDTO.getName(), result.get(0).getName());
    }

    @Test
    void shouldGetPlanById() {
        when(planRepository.findById(any())).thenReturn(Optional.of(plan));
        when(planMapper.toDTO(plan)).thenReturn(planDTO);

        PlanDTO result = planService.getPlanById(UUID.randomUUID());

        assertNotNull(result);
        assertEquals(planDTO.getName(), result.getName());
        assertEquals(planDTO.getType(), result.getType());
    }

    @Test
    void shouldThrowExceptionWhenPlanNotFound() {
        when(planRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            planService.getPlanById(UUID.randomUUID());
        });
    }
}
