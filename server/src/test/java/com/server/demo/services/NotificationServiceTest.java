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
    @DisplayName("Atualizar status de leitura com ID inválido deve falhar")
    void updateReadWithInvalidId() {
        UUID id = UUID.randomUUID();
        
        when(notificationRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> notificationService.updateRead(id, true));
        
        assertEquals("Notification not found", exception.getMessage());
        verify(notificationRepository).findById(id);
        verify(notificationRepository, never()).save(any());
        verify(eventPublisher, never()).publishEvent(any());
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
        UUID receiverId = UUID.randomUUID();
        List<Notification> notifications = List.of(Instancio.create(Notification.class));
        List<NotificationDTO> responseDTOs = List.of(Instancio.create(NotificationDTO.class));

        when(notificationRepository.findUnreadNotificationsByReceiverId(receiverId)).thenReturn(notifications);
        when(notificationMapper.toDTOList(notifications)).thenReturn(responseDTOs);

        List<NotificationDTO> data = notificationService.getUnreadNotifications(receiverId);

        assertEquals(responseDTOs, data);
        verify(notificationRepository).findUnreadNotificationsByReceiverId(receiverId);
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
        
        assertEquals("Notification not found", exception.getMessage());
        verify(notificationRepository).findById(id);
        verify(notificationRepository, never()).delete(any());
        verify(eventPublisher, never()).publishEvent(any());
    }

}