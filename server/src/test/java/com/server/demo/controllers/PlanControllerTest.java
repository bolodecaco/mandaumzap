package com.server.demo.controllers;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.server.demo.config.SecurityTestConfig;
import com.server.demo.dtos.PlanDTO;
import com.server.demo.enums.PlanType;
import com.server.demo.services.PlanService;

@WebMvcTest(controllers = PlanController.class)
@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlanService planService;

    private PlanDTO planDTO;

    @BeforeEach
    void setUp() {
        planDTO = new PlanDTO();
        planDTO.setId(UUID.randomUUID());
        planDTO.setName("Plano Test");
        planDTO.setType(PlanType.PREMIUM);
        planDTO.setPrice(99.9);
        planDTO.setBenefits("Benefícios de teste");
    }

    @Test
    void shouldGetAllPlans() throws Exception {
        when(planService.getAllPlans()).thenReturn(Arrays.asList(planDTO));

        mockMvc.perform(get("/api/public/plans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Plano Test"));
    }

    @Test
    void shouldGetPlanById() throws Exception {
        when(planService.getPlanById(any())).thenReturn(planDTO);

        mockMvc.perform(get("/api/public/plans/" + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Plano Test"));
    }
}
