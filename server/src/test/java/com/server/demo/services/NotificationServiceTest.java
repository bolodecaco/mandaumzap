package com.server.demo.services;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.dtos.RequestNotificationDTO;
import com.server.demo.mappers.NotificationMapper;
import com.server.demo.models.Notification;
import com.server.demo.models.User;
import com.server.demo.repositories.NotificationRepository;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Create Notification")
    void createNotification() {
        UUID receiverId = UUID.randomUUID();
        RequestNotificationDTO requestNotificationDTO = new RequestNotificationDTO();
        requestNotificationDTO.setReceiverId(receiverId);
        User receiver = new User();
        receiver.setId(receiverId);
        Notification notification = new Notification();
        notification.setReceiver(receiver);
        NotificationDTO expectedNotificationDTO = new NotificationDTO();

        when(notificationMapper.toEntity(requestNotificationDTO)).thenReturn(notification);
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(notificationMapper.toDTO(notification)).thenReturn(expectedNotificationDTO);

        NotificationDTO result = notificationService.createNotification(requestNotificationDTO);

        assertNotNull(result);
        verify(notificationMapper).toEntity(requestNotificationDTO);
        verify(notificationRepository).save(notification);
        verify(eventPublisher).publishEvent(any());
        verify(notificationMapper).toDTO(notification);
    }

    @Test
    @DisplayName("Get notifications by receiver id")
    void getNotificationByReceiverId() {
        UUID receiverId = UUID.randomUUID();
        Notification notification = new Notification();

        when(notificationRepository.findByReceiverId(receiverId)).thenReturn(Arrays.asList(notification));

        notificationService.getNotificationByReceiverId(receiverId);

        verify(notificationRepository).findByReceiverId(receiverId);
    }

    @Test
    @DisplayName("Update read status")
    void updateRead() {
        UUID notificationId = UUID.randomUUID();
        UUID receiverId = UUID.randomUUID();
        User receiver = new User();
        receiver.setId(receiverId);
        Notification notification = new Notification();
        notification.setId(notificationId);
        notification.setReceiver(receiver);
        notification.setRead(false);
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        notificationService.updateRead(notificationId, true);
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).save(notification);
    }

}
