package com.server.demo.services;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.enums.PlanType;
import com.server.demo.mappers.PlanMapper;
import com.server.demo.models.Plan;
import com.server.demo.repositories.PlanRepository;

@Service
public class PlanService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PlanMapper planMapper;

    public List<PlanDTO> getAllPlans() {
        List<Plan> plans = planRepository.findAll();
        return planMapper.toDTOList(plans);
    }

    private PlanDTO getPlanByType(PlanType type) {
        Plan plan = planRepository.findByType(type).orElseThrow(() -> new RuntimeException("Plan not found"));
        if (plan == null) {
            throw new RuntimeException("Plan not found for type: " + type);
        }
        return planMapper.toDTO(plan);
    }

    public PlanDTO getPlanByType(String type) {
        PlanType planType;
        try {
            planType = PlanType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid plan type: " + type);
        }
        return getPlanByType(planType);
    }

    public PlanDTO getPlanById(UUID id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new RuntimeException("Plan not found"));
        return planMapper.toDTO(plan);
    }

    public PlanDTO createPlan(Plan plan) {
        Plan currentPlan = planRepository.save(plan);
        return planMapper.toDTO(currentPlan);
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
        return planMapper.toDTO(updatedPlan);
    }

    public void deletePlan(UUID id) {
        planRepository.deleteById(id);
    }

}
