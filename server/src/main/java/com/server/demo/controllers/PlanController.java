package com.server.demo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.services.PlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/public/plans")
@Tag(name = "Plano", description = "As APIs de planos")
public class PlanController {

    @Autowired
    private PlanService planService;

    @Operation(summary = "Retorna todos os planos")
    @GetMapping
    public List<PlanDTO> getAllPlans() {
        return planService.getAllPlans();
    }

    @Operation(summary = "Retorna um plano pelo ID")
    @GetMapping("/{id}")
    public PlanDTO getPlanById(@PathVariable UUID id) {
        return planService.getPlanById(id);
    }
}
