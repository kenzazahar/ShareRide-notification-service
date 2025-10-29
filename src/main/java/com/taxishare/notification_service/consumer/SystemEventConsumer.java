package com.taxishare.notification_service.consumer;

import com.taxishare.notification_service.model.NotificationType;
import com.taxishare.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "system.alert", groupId = "notification-service-group")
    public void handleSystemAlert(String message) {
        log.info("🔔 Event reçu [system.alert]: {}", message);

        notificationService.sendNotification(
                "user123",
                "Alerte système",
                "Maintenance programmée ce soir de 23h à 2h",
                NotificationType.SYSTEM_ALERT,
                null
        );
    }

    @KafkaListener(topics = "driver.verified", groupId = "notification-service-group")
    public void handleDriverVerified(String message) {
        log.info("🔔 Event reçu [driver.verified]: {}", message);

        notificationService.sendNotification(
                "driver789",
                "Compte vérifié ✓",
                "Félicitations ! Votre compte conducteur a été vérifié",
                NotificationType.DRIVER_VERIFIED,
                null
        );
    }

    @KafkaListener(topics = "user.updated", groupId = "notification-service-group")
    public void handleUserUpdated(String message) {
        log.info("🔔 Event reçu [user.updated]: {}", message);

        notificationService.sendNotification(
                "user123",
                "Profil mis à jour",
                "Vos informations ont été mises à jour avec succès",
                NotificationType.USER_UPDATED,
                null
        );
    }

    @KafkaListener(topics = "taxi.location.updated", groupId = "notification-service-group")
    public void handleTaxiLocationUpdated(String message) {
        log.debug("📍 Position taxi mise à jour: {}", message);
        // Note: Ce topic génère beaucoup de messages
        // On ne crée pas de notification, juste un log
        // Le frontend se connecte directement via WebSocket pour le tracking
    }
}