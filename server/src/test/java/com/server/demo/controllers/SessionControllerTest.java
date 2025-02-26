package com.server.demo.controllers;

import java.util.Arrays;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.config.SecurityTestConfig;
import com.server.demo.controllers.user.SessionController;
import com.server.demo.dtos.BotConnectionDTO;
import com.server.demo.dtos.SessionDTO;
import com.server.demo.dtos.UpdateSessionDTO;
import com.server.demo.enums.ConnectionStatusType;
import com.server.demo.services.JwtService;
import com.server.demo.services.SessionService;

@WebMvcTest(controllers = SessionController.class)
@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private SessionDTO sessionDTO;
    private BotConnectionDTO botConnectionDTO;
    private UpdateSessionDTO updateSessionDTO;

    @BeforeEach
    void setUp() {
        sessionDTO = new SessionDTO();
        sessionDTO.setId(UUID.randomUUID());
        sessionDTO.setStatus(ConnectionStatusType.pending);

        botConnectionDTO = new BotConnectionDTO();
        botConnectionDTO.setId(sessionDTO.getId());
        botConnectionDTO.setStatus("pending");
        botConnectionDTO.setQrcode("data:image/png;base64,testqrcode");

        updateSessionDTO = new UpdateSessionDTO();
        updateSessionDTO.setStatus(ConnectionStatusType.open);

        when(jwtService.getCurrentUserId()).thenReturn("testUserId");
    }

    @Test
    void shouldGetAllSessions() throws Exception {
        when(sessionService.getAllSessions(anyString())).thenReturn(Arrays.asList(sessionDTO));

        mockMvc.perform(get("/api/user/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sessionDTO.getId().toString()))
                .andExpect(jsonPath("$[0].status").value(sessionDTO.getStatus().toString()));
    }

    @Test
    void shouldGetSessionById() throws Exception {
        when(sessionService.getSessionById(any(UUID.class), anyString())).thenReturn(sessionDTO);

        mockMvc.perform(get("/api/user/sessions/" + sessionDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionDTO.getId().toString()))
                .andExpect(jsonPath("$.status").value(sessionDTO.getStatus().toString()));
    }

    @Test
    void shouldCreateSession() throws Exception {
        when(sessionService.createSession(anyString())).thenReturn(botConnectionDTO);

        mockMvc.perform(post("/api/user/sessions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(botConnectionDTO.getId().toString()))
                .andExpect(jsonPath("$.status").value(botConnectionDTO.getStatus().toString()))
                .andExpect(jsonPath("$.qrcode").value(botConnectionDTO.getQrcode()));
    }

    @Test
    void shouldStartSession() throws Exception {
        when(sessionService.startSession(any(UUID.class), anyString())).thenReturn(botConnectionDTO);

        mockMvc.perform(patch("/api/user/sessions/" + sessionDTO.getId() + "/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(botConnectionDTO.getId().toString()))
                .andExpect(jsonPath("$.status").value(botConnectionDTO.getStatus().toString()))
                .andExpect(jsonPath("$.qrcode").value(botConnectionDTO.getQrcode()));
    }

    @Test
    void shouldUpdateSessionActivity() throws Exception {
        when(sessionService.updateSessionActivity(any(UUID.class), any(UpdateSessionDTO.class), anyString()))
                .thenReturn(sessionDTO);

        mockMvc.perform(patch("/api/user/sessions/" + sessionDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateSessionDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(sessionDTO.getId().toString()))
                .andExpect(jsonPath("$.status").value(sessionDTO.getStatus().toString()));
    }

    @Test
    void shouldDeleteSession() throws Exception {
        mockMvc.perform(delete("/api/user/sessions/" + sessionDTO.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCloseSession() throws Exception {
        mockMvc.perform(patch("/api/user/sessions/" + sessionDTO.getId() + "/close"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteAllSessions() throws Exception {
        mockMvc.perform(delete("/api/user/sessions/"))
                .andExpect(status().isNoContent());
    }
}
