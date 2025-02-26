package com.server.demo.controllers;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.config.SecurityTestConfig;
import com.server.demo.controllers.user.RoutineController;
import com.server.demo.dtos.RequestRoutineDTO;
import com.server.demo.dtos.RoutineDTO;
import com.server.demo.dtos.UpdateRoutineDTO;
import com.server.demo.enums.FrequencyType;
import com.server.demo.scheduler.DynamicScheduler;
import com.server.demo.services.JwtService;
import com.server.demo.services.RoutineService;

@WebMvcTest(controllers = RoutineController.class)
@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
class RoutineControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoutineService routineService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private DynamicScheduler dynamicScheduler;

    @Autowired
    private ObjectMapper objectMapper;

    private RoutineDTO routineDTO;
    private RequestRoutineDTO requestRoutineDTO;
    private UpdateRoutineDTO updateRoutineDTO;

    @BeforeEach
    void setUp() {
        routineDTO = new RoutineDTO();
        routineDTO.setId(UUID.randomUUID());
        routineDTO.setTitle("Test Routine");
        routineDTO.setCron("0 0 12 * * ?");
        routineDTO.setLastActiveAt(new Date());
        routineDTO.setMessageId(UUID.randomUUID());

        requestRoutineDTO = new RequestRoutineDTO();
        requestRoutineDTO.setTitle("New Routine");
        requestRoutineDTO.setFrequency(FrequencyType.DAILY);
        requestRoutineDTO.setMessageId(UUID.randomUUID());
        requestRoutineDTO.setExecutionDateTime("10:00");

        updateRoutineDTO = new UpdateRoutineDTO();
        updateRoutineDTO.setTitle("Updated Routine");
        updateRoutineDTO.setWillActiveAt(new Date());

        when(jwtService.getCurrentUserId()).thenReturn("testUserId");
    }

    @Test
    void shouldGetAllRoutines() throws Exception {
        when(routineService.getAllRoutines(anyString())).thenReturn(Arrays.asList(routineDTO));

        mockMvc.perform(get("/api/user/routines"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(routineDTO.getId().toString()))
                .andExpect(jsonPath("$[0].title").value(routineDTO.getTitle())) // Corrigido de name para title
                .andExpect(jsonPath("$[0].cron").value(routineDTO.getCron()));  // Corrigido de cronExpression para cron
    }

    @Test
    void shouldGetRoutineById() throws Exception {
        when(routineService.getRoutineById(any(UUID.class), anyString())).thenReturn(routineDTO);

        mockMvc.perform(get("/api/user/routines/" + routineDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(routineDTO.getId().toString()))
                .andExpect(jsonPath("$.title").value(routineDTO.getTitle()));  // Corrigido de name para title
    }

    @Test
    void shouldCreateRoutine() throws Exception {
        when(routineService.createRoutine(any(RequestRoutineDTO.class), anyString())).thenReturn(routineDTO);

        mockMvc.perform(post("/api/user/routines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestRoutineDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(routineDTO.getId().toString()))
                .andExpect(jsonPath("$.title").value(routineDTO.getTitle()));  // Corrigido de name para title
    }

    @Test
    void shouldUpdateRoutine() throws Exception {
        when(routineService.updateRoutine(any(UUID.class), any(UpdateRoutineDTO.class), anyString()))
                .thenReturn(routineDTO);

        mockMvc.perform(put("/api/user/routines/" + routineDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRoutineDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(routineDTO.getId().toString()))
                .andExpect(jsonPath("$.title").value(routineDTO.getTitle()));  // Corrigido de name para title
    }
}
