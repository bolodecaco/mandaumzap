package com.server.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.server.demo.repositories.BroadcastListRepository;

class BroadcastListServiceTest {

    @Mock
    BroadcastListRepository broadcastListRepository;

    @InjectMocks
    BroadcastListService broadcastListService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
