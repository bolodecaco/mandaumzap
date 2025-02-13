package com.server.demo.controllers.admin;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.dtos.PlanDTO;
import com.server.demo.dtos.RequestPlanDTO;
import com.server.demo.services.PlanService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/plans")
@Tag(name = "Plano", description = "As APIs de planos")
public class PlanAdminController {

    @Autowired
    private PlanService planService;


    @Operation(summary = "Criar um novo plano")
    @PostMapping("/")
    public ResponseEntity<PlanDTO> createPlan(@Valid @RequestBody RequestPlanDTO createPlanDTO) {
        return ResponseEntity.ok(planService.createPlan(createPlanDTO));
    }
}
