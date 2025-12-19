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
public class RatingEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "rating.request", groupId = "notification-service-group")
    public void handleRatingRequest(String message) {
        log.info("🔔 Event reçu [rating.request]: {}", message);

        try {
            // Exemple de JSON : {"userId":"xxx","rideId":"yyy"}
            var event = objectMapper.readTree(message);
            String userId = event.get("userId").asText();
            String rideId = event.has("rideId") ? event.get("rideId").asText() : null;

            notificationService.sendNotification(
                    userId,
                    "Évaluez votre course",
                    "Comment s'est passé votre trajet ?",
                    NotificationType.RATING_REQUEST,
                    rideId
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour rating.request: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "rating.created", groupId = "notification-service-group")
    public void handleRatingCreated(String message) {
        log.info("🔔 Event reçu [rating.created]: {}", message);

        try {
            // Exemple de JSON : {"driverId":"xxx","score":4.5}
            var event = objectMapper.readTree(message);
            String driverId = event.get("driverId").asText();

            notificationService.sendNotification(
                    driverId,
                    "Nouvelle évaluation",
                    "Un passager vous a évalué. Consultez votre score",
                    NotificationType.RATING_RECEIVED,
                    null
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour rating.created: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "driver.score.updated", groupId = "notification-service-group")
    public void handleDriverScoreUpdated(String message) {
        log.info("🔔 Event reçu [driver.score.updated]: {}", message);

        try {
            // Exemple de JSON : {"driverId":"xxx","newScore":4.8}
            var event = objectMapper.readTree(message);
            String driverId = event.get("driverId").asText();
            double newScore = event.get("newScore").asDouble();

            notificationService.sendNotification(
                    driverId,
                    "Score mis à jour",
                    String.format("Votre note moyenne est maintenant de %.1f ⭐", newScore),
                    NotificationType.DRIVER_SCORE_UPDATED,
                    null
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour driver.score.updated: {}", e.getMessage());
        }
    }
}