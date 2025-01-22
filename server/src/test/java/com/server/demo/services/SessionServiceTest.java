package com.server.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.server.demo.repositories.SessionRepository;

class SessionServiceTest {

    @Mock
    SessionRepository sessionRepository;

    @InjectMocks
    SessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
