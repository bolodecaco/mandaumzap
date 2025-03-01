package com.server.demo.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.dtos.RequestPlanDTO;
import com.server.demo.dtos.UpdatePlanDTO;
import com.server.demo.enums.PlanType;
import com.server.demo.exception.BusinessException;
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
        Plan plan = planRepository.findByType(type).orElseThrow(() -> new BusinessException(String.format("Plano de tipo %s não encontrado.", type)));
        return planMapper.toDTO(plan);
    }

    public PlanDTO getPlanByType(String type) {
        PlanType planType;
        try {
            planType = PlanType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Plano com tipo inválido: " + type);
        }
        return getPlanByType(planType);
    }

    public PlanDTO getPlanById(UUID id) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new BusinessException(String.format("Plano de ID %s não encontrado", id)));
        return planMapper.toDTO(plan);
    }

    public PlanDTO createPlan(RequestPlanDTO plan) {
        Plan currentPlan = planMapper.toEntityByRequestPlan(plan);
        planRepository.save(currentPlan);
        return planMapper.toDTO(currentPlan);
    }

    public PlanDTO updatePlan(UUID id, UpdatePlanDTO planDetails) {
        Plan plan = planRepository.findById(id).orElseThrow(() -> new BusinessException(String.format("Plano de ID %s não encontrado", id)));
        planMapper.updateEntityFromDTO(planDetails, plan);
        planRepository.save(plan);
        return planMapper.toDTO(plan);
    }

    public void deletePlan(UUID id) {
        planRepository.deleteById(id);
    }

}