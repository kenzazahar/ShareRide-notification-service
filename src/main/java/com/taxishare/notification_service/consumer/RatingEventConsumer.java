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
public class RatingEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "rating.request", groupId = "notification-service-group")
    public void handleRatingRequest(String message) {
        log.info("🔔 Event reçu [rating.request]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Évaluez votre course",
                "Comment s'est passé votre trajet ?",
                NotificationType.RATING_REQUEST,
                "ride456"
        );
    }

    @KafkaListener(topics = "rating.created", groupId = "notification-service-group")
    public void handleRatingCreated(String message) {
        log.info("🔔 Event reçu [rating.created]: {}", message);

        notificationService.sendNotification(
                "driver789",
                "Nouvelle évaluation",
                "Un passager vous a évalué. Consultez votre score",
                NotificationType.RATING_RECEIVED,
                null
        );
    }

    @KafkaListener(topics = "driver.score.updated", groupId = "notification-service-group")
    public void handleDriverScoreUpdated(String message) {
        log.info("🔔 Event reçu [driver.score.updated]: {}", message);

        notificationService.sendNotification(
                "driver789",
                "Score mis à jour",
                "Votre note moyenne est maintenant de 4.8 ⭐",
                NotificationType.DRIVER_SCORE_UPDATED,
                null
        );
    }
}