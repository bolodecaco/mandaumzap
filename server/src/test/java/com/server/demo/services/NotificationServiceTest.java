package com.server.demo.services;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.dtos.RequestNotificationDTO;
import com.server.demo.models.Notification;
import com.server.demo.mappers.NotificationMapper;
import com.server.demo.repositories.NotificationRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import com.server.demo.events.NotificationEvent;
import com.server.demo.models.User;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Criar notificação com dados válidos")
    void createNotificationWithValidData() {
        RequestNotificationDTO requestDTO = Instancio.create(RequestNotificationDTO.class);
        Notification notification = Instancio.create(Notification.class);
        notification.setReceiver(new User());
        notification.getReceiver().setId(UUID.randomUUID());
        NotificationDTO responseDTO = Instancio.create(NotificationDTO.class);

        when(notificationMapper.toEntity(requestDTO)).thenReturn(notification);
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(notificationMapper.toDTO(notification)).thenReturn(responseDTO);
        doNothing().when(eventPublisher).publishEvent(any(NotificationEvent.class));

        NotificationDTO data = notificationService.createNotification(requestDTO);

        assertEquals(responseDTO, data);
        verify(notificationMapper).toEntity(requestDTO);
        verify(notificationRepository).save(notification);
        verify(notificationMapper).toDTO(notification);
        verify(eventPublisher).publishEvent(any(NotificationEvent.class));
    }

    @Test
    @DisplayName("Atualizar status de leitura da notificação")
    void updateNotificationReadStatus() {
        UUID id = UUID.randomUUID();
        Notification notification = Instancio.create(Notification.class);
        notification.setReceiver(new User());
        notification.getReceiver().setId(UUID.randomUUID());
        NotificationDTO responseDTO = Instancio.create(NotificationDTO.class);

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(notificationMapper.toDTO(notification)).thenReturn(responseDTO);
        doNothing().when(eventPublisher).publishEvent(any(NotificationEvent.class));

        NotificationDTO data = notificationService.updateRead(id, true);

        assertEquals(responseDTO, data);
        assertTrue(notification.isRead());
        verify(notificationRepository).findById(id);
        verify(notificationRepository).save(notification);
        verify(eventPublisher).publishEvent(any(NotificationEvent.class));
    }

    @Test
    @DisplayName("Buscar notificações por ID do receptor")
    void getUserNotifications() {
        UUID receiverId = UUID.randomUUID();
        List<Notification> notifications = List.of(Instancio.create(Notification.class));
        List<NotificationDTO> responseDTOs = List.of(Instancio.create(NotificationDTO.class));

        when(notificationRepository.findByReceiverId(receiverId)).thenReturn(notifications);
        when(notificationMapper.toDTOList(notifications)).thenReturn(responseDTOs);

        List<NotificationDTO> data = notificationService.getNotificationByReceiverId(receiverId);

        assertEquals(responseDTOs, data);
        verify(notificationRepository).findByReceiverId(receiverId);
        verify(notificationMapper).toDTOList(notifications);
    }
}
