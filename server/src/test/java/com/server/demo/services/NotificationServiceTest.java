package com.server.demo.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.dtos.RequestNotificationDTO;
import com.server.demo.exception.BusinessException;
import com.server.demo.handlers.NotificationHandler;
import com.server.demo.mappers.NotificationMapper;
import com.server.demo.models.Notification;
import com.server.demo.repositories.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private NotificationHandler notificationHandler;

    @Mock
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
    private NotificationDTO notificationDTO;
    private RequestNotificationDTO requestNotificationDTO;
    private String receiverId;

    @BeforeEach
    void setUp() {
        receiverId = "testReceiverId";
        
        notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setContent("Test Notification");
        notification.setType("TEST");
        notification.setRead(false);
        notification.setReceiverId(receiverId);

        notificationDTO = new NotificationDTO();
        notificationDTO.setId(notification.getId());
        notificationDTO.setContent(notification.getContent());
        notificationDTO.setType(notification.getType());
        notificationDTO.setRead(notification.isRead());

        requestNotificationDTO = new RequestNotificationDTO();
        requestNotificationDTO.setContent("New Notification");
        requestNotificationDTO.setType("TEST");
        requestNotificationDTO.setReceiverId(receiverId);
    }

    @Test
    void shouldGetAllNotifications() {
        when(notificationRepository.findAll()).thenReturn(Arrays.asList(notification));
        when(notificationMapper.toDTOList(any())).thenReturn(Arrays.asList(notificationDTO));

        List<NotificationDTO> result = notificationService.getAllNotifications();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notificationDTO.getContent(), result.get(0).getContent());
    }

    @Test
    void shouldGetNotificationsByReceiverId() {
        when(notificationRepository.findByReceiverId(anyString())).thenReturn(Arrays.asList(notification));
        when(notificationMapper.toDTOList(any())).thenReturn(Arrays.asList(notificationDTO));

        List<NotificationDTO> result = notificationService.getNotificationByReceiverId(receiverId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notificationDTO.getContent(), result.get(0).getContent());
    }

    @Test
    void shouldCreateNotification() {
        when(notificationMapper.toEntity(any())).thenReturn(notification);
        when(notificationRepository.save(any())).thenReturn(notification);
        when(notificationMapper.toDTO(any())).thenReturn(notificationDTO);

        NotificationDTO result = notificationService.createNotification(requestNotificationDTO);

        assertNotNull(result);
        assertEquals(notificationDTO.getContent(), result.getContent());
    }

    @Test
    void shouldUpdateNotificationRead() {
        when(notificationRepository.findByIdAndReceiverId(any(), anyString()))
            .thenReturn(Optional.of(notification));
        when(notificationRepository.save(any())).thenReturn(notification);
        when(notificationMapper.toDTO(any())).thenReturn(notificationDTO);

        NotificationDTO result = notificationService.updateRead(notification.getId(), true, receiverId);

        assertNotNull(result);
        assertEquals(notificationDTO.getContent(), result.getContent());
    }

    @Test
    void shouldThrowExceptionWhenNotificationNotFound() {
        when(notificationRepository.findByIdAndReceiverId(any(), anyString()))
            .thenReturn(Optional.empty());

        assertThrows(BusinessException.class, () -> {
            notificationService.updateRead(UUID.randomUUID(), true, receiverId);
        });
    }

    @Test
    void shouldGetUnreadNotifications() {
        when(notificationRepository.findUnreadNotificationsByReceiverId(anyString()))
            .thenReturn(Arrays.asList(notification));
        when(notificationMapper.toDTOList(any())).thenReturn(Arrays.asList(notificationDTO));

        List<NotificationDTO> result = notificationService.getUnreadNotifications(receiverId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notificationDTO.getContent(), result.get(0).getContent());
    }
}
