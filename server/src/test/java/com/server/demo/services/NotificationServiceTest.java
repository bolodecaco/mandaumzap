package com.server.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.instancio.Instancio;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.server.demo.dtos.NotificationDTO;
import com.server.demo.dtos.RequestNotificationDTO;
import com.server.demo.events.NotificationEvent;
import com.server.demo.mappers.NotificationMapper;
import com.server.demo.models.Notification;
import com.server.demo.repositories.NotificationRepository;

class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private org.springframework.context.ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private NotificationService notificationService;

    private String userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID().toString();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Criar notificação com dados válidos")
    void createNotificationWithValidData() {
        RequestNotificationDTO requestDTO = Instancio.create(RequestNotificationDTO.class);
        Notification notification = Instancio.create(Notification.class);
        notification.setReceiverId(userId);
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
        notification.setReceiverId(userId);
        notification.getReceiverId();
        NotificationDTO responseDTO = Instancio.create(NotificationDTO.class);

        when(notificationRepository.findByIdAndReceiverId(id, userId)).thenReturn(Optional.of(notification));
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(notificationMapper.toDTO(notification)).thenReturn(responseDTO);
        doNothing().when(eventPublisher).publishEvent(any(NotificationEvent.class));

        NotificationDTO data = notificationService.updateRead(id, true, userId);

        assertEquals(responseDTO, data);
        assertTrue(notification.isRead());
        verify(notificationRepository).findByIdAndReceiverId(id, userId);
        verify(notificationRepository).save(notification);
        verify(eventPublisher).publishEvent(any(NotificationEvent.class));
    }

    @Test
    @DisplayName("Atualizar status de leitura com ID inválido deve falhar")
    void updateReadWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(notificationRepository.findByIdAndReceiverId(id, userId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> notificationService.updateRead(id, true, userId));

        assertEquals("Notificação com id " + id + " não encontrada.",
                exception.getMessage());
        verify(notificationRepository).findByIdAndReceiverId(id, userId);
        verify(notificationRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

    @Test
    @DisplayName("Buscar notificações por ID do receptor")
    void getUserNotifications() {
        List<Notification> notifications = List.of(Instancio.create(Notification.class));
        List<NotificationDTO> responseDTOs = List.of(Instancio.create(NotificationDTO.class));

        when(notificationRepository.findByReceiverId(userId)).thenReturn(notifications);
        when(notificationMapper.toDTOList(notifications)).thenReturn(responseDTOs);

        List<NotificationDTO> data = notificationService.getNotificationByReceiverId(userId);

        assertEquals(responseDTOs, data);
        verify(notificationRepository).findByReceiverId(userId);
        verify(notificationMapper).toDTOList(notifications);
    }

    @Test
    @DisplayName("Buscar todas as notificações")
    void getAllNotifications() {
        List<Notification> notifications = List.of(Instancio.create(Notification.class));
        List<NotificationDTO> responseDTOs = List.of(Instancio.create(NotificationDTO.class));

        when(notificationRepository.findAll()).thenReturn(notifications);
        when(notificationMapper.toDTOList(notifications)).thenReturn(responseDTOs);

        List<NotificationDTO> data = notificationService.getAllNotifications();

        assertEquals(responseDTOs, data);
        verify(notificationRepository).findAll();
        verify(notificationMapper).toDTOList(notifications);
    }

    @Test
    @DisplayName("Buscar notificações não lidas")
    void getUnreadNotifications() {
        List<Notification> notifications = List.of(Instancio.create(Notification.class));
        List<NotificationDTO> responseDTOs = List.of(Instancio.create(NotificationDTO.class));

        when(notificationRepository.findUnreadNotificationsByReceiverId(userId)).thenReturn(notifications);
        when(notificationMapper.toDTOList(notifications)).thenReturn(responseDTOs);

        List<NotificationDTO> data = notificationService.getUnreadNotifications(userId);

        assertEquals(responseDTOs, data);
        verify(notificationRepository).findUnreadNotificationsByReceiverId(userId);
        verify(notificationMapper).toDTOList(notifications);
    }

    @Test
    @DisplayName("Deletar notificação existente")
    void deleteNotification() {
        UUID id = UUID.randomUUID();
        Notification notification = Instancio.create(Notification.class);

        when(notificationRepository.findById(id)).thenReturn(Optional.of(notification));
        doNothing().when(notificationRepository).delete(notification);
        doNothing().when(eventPublisher).publishEvent(any(NotificationEvent.class));

        assertDoesNotThrow(() -> notificationService.deleteNotification(id));

        verify(notificationRepository).findById(id);
        verify(notificationRepository).delete(notification);
        verify(eventPublisher).publishEvent(any(NotificationEvent.class));
    }

    @Test
    @DisplayName("Deletar notificação com ID inválido deve falhar")
    void deleteNotificationWithInvalidId() {
        UUID id = UUID.randomUUID();

        when(notificationRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> notificationService.deleteNotification(id));

        assertEquals("Notificação com id " + id + " não encontrada.", exception.getMessage());
        verify(notificationRepository).findById(id);
        verify(notificationRepository, never()).delete(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

}
