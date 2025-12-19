package com.taxishare.notification_service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxishare.notification_service.event.*;
import com.taxishare.notification_service.model.NotificationType;
import com.taxishare.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RideEventConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "ride.accepted", groupId = "notification-service-group")
    public void handleRideAccepted(String message) {
        log.info("🔔 Event reçu [ride.accepted]: {}", message);

        try {
            // ✅ Parser le JSON
            RideAcceptedEvent event = objectMapper.readValue(message, RideAcceptedEvent.class);

            // ✅ Utiliser le vrai userId du JSON
            notificationService.sendNotification(
                    event.getPassengerId(),  // ← Vrai userId du JSON !
                    "Course acceptée !",
                    String.format("Le conducteur %s arrive dans %s",
                            event.getDriverName() != null ? event.getDriverName() : "votre conducteur",
                            event.getEta() != null ? event.getEta() : "quelques minutes"),
                    NotificationType.RIDE_ACCEPTED,
                    event.getRideId()
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour ride.accepted: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "ride.started", groupId = "notification-service-group")
    public void handleRideStarted(String message) {
        log.info("🔔 Event reçu [ride.started]: {}", message);

        try {
            RideAcceptedEvent event = objectMapper.readValue(message, RideAcceptedEvent.class);

            notificationService.sendNotification(
                    event.getPassengerId(),
                    "Course en cours",
                    "Bon voyage ! Arrivée prévue bientôt",
                    NotificationType.RIDE_STARTED,
                    event.getRideId()
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour ride.started: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "ride.completed", groupId = "notification-service-group")
    public void handleRideCompleted(String message) {
        log.info("🔔 Event reçu [ride.completed]: {}", message);

        try {
            RideAcceptedEvent event = objectMapper.readValue(message, RideAcceptedEvent.class);

            // Notifier le passager
            notificationService.sendNotification(
                    event.getPassengerId(),
                    "Course terminée",
                    "Merci ! N'oubliez pas d'évaluer votre conducteur",
                    NotificationType.RIDE_COMPLETED,
                    event.getRideId()
            );

            // Notifier le conducteur
            if (event.getDriverId() != null) {
                notificationService.sendNotification(
                        event.getDriverId(),
                        "Course terminée",
                        "Évaluez votre expérience avec les passagers",
                        NotificationType.RATING_REQUEST,
                        event.getRideId()
                );
            }
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour ride.completed: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "passenger.joined", groupId = "notification-service-group")
    public void handlePassengerJoined(String message) {
        log.info("🔔 Event reçu [passenger.joined]: {}", message);

        try {
            PassengerJoinedEvent event = objectMapper.readValue(message, PassengerJoinedEvent.class);

            // Notifier les passagers existants
            if (event.getExistingPassengerIds() != null) {
                for (String passengerId : event.getExistingPassengerIds()) {
                    notificationService.sendNotification(
                            passengerId,
                            "Nouveau passager",
                            String.format("Un passager a rejoint votre course. %s",
                                    event.getNewEta() != null ? "Nouvelle arrivée: " + event.getNewEta() : ""),
                            NotificationType.PASSENGER_JOINED,
                            event.getRideId()
                    );
                }
            }

            // Notifier le nouveau passager
            notificationService.sendNotification(
                    event.getNewPassengerId(),
                    "Bienvenue à bord !",
                    "Vous avez rejoint la course avec succès",
                    NotificationType.PASSENGER_JOINED,
                    event.getRideId()
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour passenger.joined: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "payment.captured", groupId = "notification-service-group")
    public void handlePaymentCaptured(String message) {
        log.info("🔔 Event reçu [payment.captured]: {}", message);

        try {
            PaymentCapturedEvent event = objectMapper.readValue(message, PaymentCapturedEvent.class);

            notificationService.sendNotification(
                    event.getUserId(),
                    "Paiement réussi",
                    String.format("Votre paiement de %.2f %s a été traité avec succès",
                            event.getAmount(),
                            event.getCurrency() != null ? event.getCurrency() : "DH"),
                    NotificationType.PAYMENT_CAPTURED,
                    event.getRideId()
            );
        } catch (Exception e) {
            log.error("❌ Erreur parsing JSON pour payment.captured: {}", e.getMessage());
        }
    }
}