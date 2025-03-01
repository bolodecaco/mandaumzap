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
import com.server.demo.controllers.user.BroadcastListController;
import com.server.demo.dtos.AddChatToBroadcastListDTO;
import com.server.demo.dtos.BroadcastListDTO;
import com.server.demo.dtos.ChatDTO;
import com.server.demo.dtos.RequestBroadcastListDTO;
import com.server.demo.dtos.UpdateBroadcastListDTO;
import com.server.demo.services.BroadcastListService;
import com.server.demo.services.JwtService;

@WebMvcTest(controllers = BroadcastListController.class)
@Import(SecurityTestConfig.class)
@ActiveProfiles("test")
class BroadcastListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BroadcastListService broadcastListService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    private BroadcastListDTO broadcastListDTO;
    private RequestBroadcastListDTO requestBroadcastListDTO;
    private UpdateBroadcastListDTO updateBroadcastListDTO;
    private AddChatToBroadcastListDTO addChatDTO;
    private ChatDTO chatDTO;

    @BeforeEach
    void setUp() {
        broadcastListDTO = new BroadcastListDTO();
        broadcastListDTO.setId(UUID.randomUUID());
        broadcastListDTO.setTitle("Test List");
        broadcastListDTO.setMessagesSent(0);

        requestBroadcastListDTO = new RequestBroadcastListDTO();
        requestBroadcastListDTO.setTitle("New List");

        updateBroadcastListDTO = new UpdateBroadcastListDTO();
        updateBroadcastListDTO.setTitle("Updated List");

        addChatDTO = new AddChatToBroadcastListDTO();
        addChatDTO.setChatId(UUID.randomUUID());

        chatDTO = new ChatDTO();
        chatDTO.setId(UUID.randomUUID());
        chatDTO.setChatName("Test Chat");
        chatDTO.setWhatsAppId("123456@c.us");

        when(jwtService.getCurrentUserId()).thenReturn("testUserId");
    }

    @Test
    void shouldGetAllLists() throws Exception {
        Page<BroadcastListDTO> listsPage = new PageImpl<>(Arrays.asList(broadcastListDTO));

        when(broadcastListService.findAllByUserId(anyString(), any(Pageable.class), anyString()))
                .thenReturn(listsPage);

        mockMvc.perform(get("/api/user/lists")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "lastActiveAt"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(broadcastListDTO.getId().toString()))
                .andExpect(jsonPath("$.content[0].title").value(broadcastListDTO.getTitle()))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void shouldGetListById() throws Exception {
        when(broadcastListService.getListById(any(UUID.class), anyString())).thenReturn(broadcastListDTO);

        mockMvc.perform(get("/api/user/lists/" + broadcastListDTO.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(broadcastListDTO.getId().toString()))
                .andExpect(jsonPath("$.title").value(broadcastListDTO.getTitle()));
    }

    @Test
    void shouldCreateList() throws Exception {
        when(broadcastListService.createList(any(RequestBroadcastListDTO.class), anyString())).thenReturn(broadcastListDTO);

        mockMvc.perform(post("/api/user/lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBroadcastListDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(broadcastListDTO.getId().toString()))
                .andExpect(jsonPath("$.title").value(broadcastListDTO.getTitle()));
    }

    @Test
    void shouldUpdateList() throws Exception {
        when(broadcastListService.updateList(any(UUID.class), any(UpdateBroadcastListDTO.class), anyString()))
                .thenReturn(broadcastListDTO);

        mockMvc.perform(put("/api/user/lists/" + broadcastListDTO.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateBroadcastListDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(broadcastListDTO.getId().toString()));
    }

    @Test
    void shouldAddChatsToList() throws Exception {
        when(broadcastListService.addChats(any(UUID.class), any(), anyString())).thenReturn(broadcastListDTO);

        mockMvc.perform(post("/api/user/lists/" + broadcastListDTO.getId() + "/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Arrays.asList(addChatDTO))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(broadcastListDTO.getId().toString()));
    }

    @Test
    void shouldRemoveChatFromList() throws Exception {
        when(broadcastListService.removeChat(any(UUID.class), any(), anyString())).thenReturn(broadcastListDTO);

        mockMvc.perform(delete("/api/user/lists/" + broadcastListDTO.getId() + "/chats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addChatDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(broadcastListDTO.getId().toString()));
    }

    @Test
    void shouldDeleteList() throws Exception {
        mockMvc.perform(delete("/api/user/lists/" + broadcastListDTO.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldGetChatsFromList() throws Exception {
        when(broadcastListService.getChatsFromList(any(UUID.class), anyString())).thenReturn(Arrays.asList(chatDTO));

        mockMvc.perform(get("/api/user/lists/" + broadcastListDTO.getId() + "/chats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(chatDTO.getId().toString()))
                .andExpect(jsonPath("$[0].chatName").value(chatDTO.getChatName()))
                .andExpect(jsonPath("$[0].whatsAppId").value(chatDTO.getWhatsAppId()));
    }
}
