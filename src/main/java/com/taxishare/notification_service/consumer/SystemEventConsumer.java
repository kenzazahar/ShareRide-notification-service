package com.taxishare.notification_service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "system.alert", groupId = "notification-service-group")
    public void handleSystemAlert(String message) {
        log.info("🔔 Event reçu [system.alert]: {}", message);

        try {
            var event = objectMapper.readTree(message);
            String userId = event.get("userId").asText();
            String alertMessage = event.get("message").asText();

            notificationService.sendNotification(
                    userId,
                    "Alerte système",
                    alertMessage,
                    NotificationType.SYSTEM_ALERT,
                    null
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour system.alert: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "driver.verified", groupId = "notification-service-group")
    public void handleDriverVerified(String message) {
        log.info("🔔 Event reçu [driver.verified]: {}", message);

        try {
            var event = objectMapper.readTree(message);
            String driverId = event.get("driverId").asText();

            notificationService.sendNotification(
                    driverId,
                    "Compte vérifié ✓",
                    "Félicitations ! Votre compte conducteur a été vérifié",
                    NotificationType.DRIVER_VERIFIED,
                    null
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour driver.verified: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "user.updated", groupId = "notification-service-group")
    public void handleUserUpdated(String message) {
        log.info("🔔 Event reçu [user.updated]: {}", message);

        try {
            var event = objectMapper.readTree(message);
            String userId = event.get("userId").asText();

            notificationService.sendNotification(
                    userId,
                    "Profil mis à jour",
                    "Vos informations ont été mises à jour avec succès",
                    NotificationType.USER_UPDATED,
                    null
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour user.updated: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "taxi.location.updated", groupId = "notification-service-group")
    public void handleTaxiLocationUpdated(String message) {
        log.debug("📍 Position taxi mise à jour: {}", message);
        // Pas de notification créée
    }
}