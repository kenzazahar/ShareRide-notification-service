package com.taxishare.notification_service.service;

import com.taxishare.notification_service.model.Notification;
import com.taxishare.notification_service.model.NotificationType;
import com.taxishare.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public Notification sendNotification(String userId, String title, String message, NotificationType type, String rideId) {
        log.info("📨 Envoi notification à userId={}, type={}", userId, type);

        // Sauvegarder en DB
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        notification.setRideId(rideId);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);

        // Envoyer via WebSocket
        try {
            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/queue/notifications",
                    notification
            );
            log.info("✅ Notification WebSocket envoyée avec succès");
        } catch (Exception e) {
            log.error("❌ Erreur envoi WebSocket: {}", e.getMessage());
        }

        return notification;
    }

    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserId(userId);
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification non trouvée"));
        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    public Long countUnread(String userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
}
