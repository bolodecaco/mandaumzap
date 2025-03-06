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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.config.SecurityTestConfig;
import com.server.demo.controllers.user.MessageController;
import com.server.demo.dtos.MessageDTO;
import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.services.JwtService;
import com.server.demo.services.MessageService;

@WebMvcTest(controllers = MessageController.class)
@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MessageService messageService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private MessageDTO messageDTO;
    private RequestMessageDTO requestMessageDTO;

    @BeforeEach
    void setUp() {
        messageDTO = new MessageDTO();
        messageDTO.setId(UUID.randomUUID());
        messageDTO.setContent("Test Message");
        messageDTO.setTimesSent(0);
        messageDTO.setLastSent(new Date());
        messageDTO.setSessionId(UUID.randomUUID());
        messageDTO.setBroadcastListId(UUID.randomUUID());

        requestMessageDTO = new RequestMessageDTO();
        requestMessageDTO.setContent("Test Message");
        requestMessageDTO.setSessionId(UUID.randomUUID());
        requestMessageDTO.setBroadcastListId(UUID.randomUUID());

        when(jwtService.getCurrentUserId()).thenReturn("testUserId");
    }

    @Test
    void shouldGetAllMessages() throws Exception {
        when(messageService.getActiveMessages(anyString())).thenReturn(Arrays.asList(messageDTO));

        mockMvc.perform(get("/api/user/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(messageDTO.getId().toString()))
                .andExpect(jsonPath("$[0].content").value(messageDTO.getContent()));
    }

    @Test
    void shouldGetMessageById() throws Exception {
        when(messageService.getMessageById(any(UUID.class), anyString())).thenReturn(messageDTO);

        mockMvc.perform(get("/api/user/messages/" + messageDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(messageDTO.getId().toString()))
                .andExpect(jsonPath("$.content").value(messageDTO.getContent()));
    }

    @Test
    void shouldGetMessagesBySessionId() throws Exception {
        when(messageService.getMessagesBySessionId(any(UUID.class), anyString())).thenReturn(Arrays.asList(messageDTO));

        mockMvc.perform(get("/api/user/messages/user/" + messageDTO.getSessionId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(messageDTO.getId().toString()))
                .andExpect(jsonPath("$[0].content").value(messageDTO.getContent()));
    }

    @Test
    void shouldCreateMessage() throws Exception {
        when(messageService.saveMessage(any(RequestMessageDTO.class), anyString())).thenReturn(messageDTO);

        mockMvc.perform(post("/api/user/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestMessageDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(messageDTO.getId().toString()))
                .andExpect(jsonPath("$.content").value(messageDTO.getContent()));
    }

    @Test
    void shouldSendMessage() throws Exception {
        when(messageService.sendMessage(any(UUID.class))).thenReturn(messageDTO);

        mockMvc.perform(post("/api/user/messages/" + messageDTO.getId() + "/send"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(messageDTO.getId().toString()))
                .andExpect(jsonPath("$.content").value(messageDTO.getContent()));
    }

    @Test
    void shouldDeleteMessage() throws Exception {
        mockMvc.perform(delete("/api/user/messages/" + messageDTO.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldDeleteAllMessages() throws Exception {
        mockMvc.perform(delete("/api/user/messages"))
                .andExpect(status().isNoContent());
    }
}
