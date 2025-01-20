package com.server.demo.services;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

import com.server.demo.dtos.RequestMessageDTO;
import com.server.demo.mappers.MessageMapper;
import com.server.demo.repositories.MessageRepository;

class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveMessage_ShouldSaveMessageSuccessfully() {
        RequestMessageDTO requestMessageDTO = new RequestMessageDTO();
        requestMessageDTO.setContent("Hello");
        requestMessageDTO.setOwnerId(UUID.randomUUID());
        requestMessageDTO.setBroadcastListId(UUID.randomUUID());
        messageService.saveMessage(requestMessageDTO);
        verify(messageRepository, times(1)).save(messageMapper.toEntity(requestMessageDTO));
    }
}