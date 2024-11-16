package com.server.demo.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.services.PlanService;

@RestController
@RequestMapping("/api/plans")
public class PlanController {
    
    @Autowired
    private PlanService planService;

    @GetMapping
    public List<PlanDTO> getAllPlans() {
        return planService.getAllPlans();
    }

    @GetMapping("/{id}")
    public PlanDTO getPlanById(UUID id) {
        return planService.getPlanById(id);
    }
}
