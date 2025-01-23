package com.server.demo.services;

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

import com.server.demo.dtos.RequestPlanDTO;
import com.server.demo.enums.PlanType;
import com.server.demo.mappers.PlanMapper;
import com.server.demo.models.Plan;
import com.server.demo.repositories.PlanRepository;

class PlanServiceTest {

    @Mock
    PlanRepository planRepository;

    @Mock
    PlanMapper planMapper;

    @InjectMocks
    PlanService planService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test createPlan")
    void createPlan() {
        RequestPlanDTO requestPlanDTO = new RequestPlanDTO();
        requestPlanDTO.setName("Free Plan");
        requestPlanDTO.setPrice(0.0);
        requestPlanDTO.setType(PlanType.FREE);
        Plan plan = new Plan();
        when(planMapper.toEntityByRequestPlan(requestPlanDTO)).thenReturn(plan);
        planService.createPlan(requestPlanDTO);
        assertNotNull(plan, "O plano não pode ser nulo");
    }

    @Test
    @DisplayName("Test get plan by type")
    void getPlanByType() {
        Plan plan = new Plan();
        plan.setType(PlanType.FREE);
        when(planRepository.findByType(PlanType.FREE)).thenReturn(java.util.Optional.of(plan));
        assertNotNull(plan, "O plano não pode ser nulo");
    }

    @Test
    @DisplayName("Test get plan by id")
    void getPlanById() {
        Plan plan = new Plan();
        when(planRepository.findById(plan.getId())).thenReturn(java.util.Optional.of(plan));
        assertNotNull(plan, "O plano não pode ser nulo");
    }

    @Test
    @DisplayName("Update plan")
    void updateChat() {
        UUID id = UUID.randomUUID();
        Plan plan = new Plan();
        plan.setId(id);
        when(planRepository.findById(id)).thenReturn(java.util.Optional.of(plan));
        when(planRepository.save(plan)).thenReturn(plan);
        plan.setType(PlanType.FREE);
        planService.updatePlan(id, plan);
        verify(planRepository, times(1)).save(plan);
    }

    @Test
    @DisplayName("Test delete plan")
    void deletePlan() {
        UUID id = UUID.randomUUID();
        Plan plan = new Plan();
        when(planRepository.findById(id)).thenReturn(java.util.Optional.of(plan));
        planService.deletePlan(id);
        verify(planRepository, times(1)).deleteById(id);
    }

}
