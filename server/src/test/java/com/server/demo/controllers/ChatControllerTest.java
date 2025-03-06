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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.demo.config.SecurityTestConfig;
import com.server.demo.controllers.user.ChatController;
import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestChatDTO;
import com.server.demo.dtos.UpdateChatDTO;
import com.server.demo.services.ChatService;
import com.server.demo.services.JwtService;

@WebMvcTest(controllers = ChatController.class)
@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private ChatDTO chatDTO;
    private RequestChatDTO requestChatDTO;
    private UpdateChatDTO updateChatDTO;

    @BeforeEach
    void setUp() {
        chatDTO = new ChatDTO();
        chatDTO.setId(UUID.randomUUID());
        chatDTO.setChatName("Test Chat");
        chatDTO.setWhatsAppId("123456789@g.us");
        chatDTO.setSessionId(UUID.randomUUID());

        requestChatDTO = new RequestChatDTO();
        requestChatDTO.setChatName("Test Chat");
        requestChatDTO.setWhatsAppId("123456789@g.us");
        requestChatDTO.setSessionId(UUID.randomUUID());

        updateChatDTO = new UpdateChatDTO();
        updateChatDTO.setChatName("Updated Chat");

        when(jwtService.getCurrentUserId()).thenReturn("testUserId");
    }

    @Test
    void shouldGetAllChats() throws Exception {
        Page<ChatDTO> chatPage = new PageImpl<>(Arrays.asList(chatDTO));
        when(chatService.getAllChats(anyString(), any(Pageable.class), any(), any())).thenReturn(chatPage);

        mockMvc.perform(get("/api/user/chats")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "chatName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(chatDTO.getId().toString()))
                .andExpect(jsonPath("$.content[0].chatName").value(chatDTO.getChatName()))
                .andExpect(jsonPath("$.content[0].whatsAppId").value(chatDTO.getWhatsAppId()));
    }

    @Test
    void shouldGetAllChatsWithoutSessionId() throws Exception {
        Page<ChatDTO> chatPage = new PageImpl<>(Arrays.asList(chatDTO));
        when(chatService.getAllChats(anyString(), any(Pageable.class), any(), any())).thenReturn(chatPage);

        mockMvc.perform(get("/api/user/chats")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "chatName"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(chatDTO.getId().toString()))
                .andExpect(jsonPath("$.content[0].chatName").value(chatDTO.getChatName()))
                .andExpect(jsonPath("$.content[0].whatsAppId").value(chatDTO.getWhatsAppId()));
    }

    @Test
    void shouldGetChatById() throws Exception {
        when(chatService.getChatDTOById(any(UUID.class), anyString())).thenReturn(chatDTO);

        mockMvc.perform(get("/api/user/chats/" + chatDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chatDTO.getId().toString()))
                .andExpect(jsonPath("$.chatName").value(chatDTO.getChatName()))
                .andExpect(jsonPath("$.whatsAppId").value(chatDTO.getWhatsAppId()));
    }

    @Test
    void shouldCreateChat() throws Exception {
        when(chatService.createChat(any(RequestChatDTO.class), anyString())).thenReturn(chatDTO);

        mockMvc.perform(post("/api/user/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestChatDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chatDTO.getId().toString()))
                .andExpect(jsonPath("$.chatName").value(chatDTO.getChatName()))
                .andExpect(jsonPath("$.whatsAppId").value(chatDTO.getWhatsAppId()));
    }

    @Test
    void shouldUpdateChat() throws Exception {
        when(chatService.updateChat(any(UUID.class), any(UpdateChatDTO.class), anyString())).thenReturn(chatDTO);

        mockMvc.perform(put("/api/user/chats/" + chatDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateChatDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(chatDTO.getId().toString()))
                .andExpect(jsonPath("$.chatName").value(chatDTO.getChatName()));
    }

    @Test
    void shouldDeleteChat() throws Exception {
        mockMvc.perform(delete("/api/user/chats/" + chatDTO.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldValidateChatCreation() throws Exception {
        RequestChatDTO invalidChat = new RequestChatDTO();
        invalidChat.setChatName("a");
        invalidChat.setWhatsAppId("invalid-id");

        mockMvc.perform(post("/api/user/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidChat)))
                .andExpect(status().isBadRequest());
    }
}
