package com.server.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.server.demo.repositories.NotificationRepository;

class NotificationServiceTest {

    @Mock
    NotificationRepository notificationRepository;

    @InjectMocks
    NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
