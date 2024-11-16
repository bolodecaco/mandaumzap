package com.server.demo.services;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.models.Plan;
import com.server.demo.repositories.PlanRepository;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    public List<PlanDTO> getAllPlans() {
        return planRepository.findAll().stream()
                .map(
                        plan -> new PlanDTO(
                                plan.getId(),
                                plan.getName(),
                                plan.getBenefits(),
                                plan.getPrice(),
                                plan.getType()
                        )
                ).collect(Collectors.toList());
    }
    
    public PlanDTO getPlanById(UUID id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan not found"));
        return new PlanDTO(plan.getId(), plan.getName(), plan.getBenefits(), plan.getPrice(), plan.getType());
    }

    public PlanDTO createPlan(Plan plan) {
        Plan currentPlan = planRepository.save(plan);
        return new PlanDTO(currentPlan.getId(), currentPlan.getName(), currentPlan.getBenefits(), currentPlan.getPrice(), currentPlan.getType());
    }

    public PlanDTO updatePlan(UUID id, Plan planDetails) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan not found"));
        if (planDetails.getName() != null) {
            plan.setName(planDetails.getName());
        }
        if (planDetails.getBenefits() != null) {
            plan.setBenefits(planDetails.getBenefits());
        }
        if (!Objects.isNull(planDetails.getPrice())) {
            plan.setPrice(planDetails.getPrice());
        }
        if (planDetails.getType() != null) {
            plan.setType(planDetails.getType());
        }
        Plan updatedPlan = planRepository.save(plan);
        return new PlanDTO(updatedPlan.getId(), updatedPlan.getName(), updatedPlan.getBenefits(), updatedPlan.getPrice(), updatedPlan.getType());

    }

    public void deletePlan(UUID id) {
        planRepository.deleteById(id);
    }

}
