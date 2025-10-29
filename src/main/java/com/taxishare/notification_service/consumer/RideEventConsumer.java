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
public class RideEventConsumer {

    private final NotificationService notificationService;

    // ============================================
    // CYCLE DE VIE DE LA COURSE
    // ============================================

    @KafkaListener(topics = "ride.created", groupId = "notification-service-group")
    public void handleRideCreated(String message) {
        log.info("🔔 Event reçu [ride.created]: {}", message);

        // TODO: Parser JSON pour extraire userId, rideId
        notificationService.sendNotification(
                "passenger123",
                "Course créée",
                "Recherche d'un conducteur en cours...",
                NotificationType.RIDE_CREATED,
                "ride456"
        );
    }

    @KafkaListener(topics = "ride.accepted", groupId = "notification-service-group")
    public void handleRideAccepted(String message) {
        log.info("🔔 Event reçu [ride.accepted]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Course acceptée !",
                "Le conducteur arrive dans 5 minutes",
                NotificationType.RIDE_ACCEPTED,
                "ride456"
        );
    }

    @KafkaListener(topics = "driver.arriving", groupId = "notification-service-group")
    public void handleDriverArriving(String message) {
        log.info("🔔 Event reçu [driver.arriving]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Le conducteur arrive",
                "Votre conducteur sera là dans 2 minutes",
                NotificationType.DRIVER_ARRIVING,
                "ride456"
        );
    }

    @KafkaListener(topics = "driver.arrived", groupId = "notification-service-group")
    public void handleDriverArrived(String message) {
        log.info("🔔 Event reçu [driver.arrived]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Le conducteur est arrivé",
                "Votre conducteur vous attend au point de rendez-vous",
                NotificationType.DRIVER_ARRIVED,
                "ride456"
        );
    }

    @KafkaListener(topics = "ride.started", groupId = "notification-service-group")
    public void handleRideStarted(String message) {
        log.info("🔔 Event reçu [ride.started]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Course en cours",
                "Bon voyage ! Arrivée prévue dans 15 minutes",
                NotificationType.RIDE_STARTED,
                "ride456"
        );
    }

    @KafkaListener(topics = "ride.completed", groupId = "notification-service-group")
    public void handleRideCompleted(String message) {
        log.info("🔔 Event reçu [ride.completed]: {}", message);

        // Notifier le passager
        notificationService.sendNotification(
                "passenger123",
                "Course terminée",
                "Merci ! N'oubliez pas d'évaluer votre conducteur",
                NotificationType.RIDE_COMPLETED,
                "ride456"
        );

        // Notifier le conducteur
        notificationService.sendNotification(
                "driver789",
                "Course terminée",
                "Évaluez votre expérience avec les passagers",
                NotificationType.RATING_REQUEST,
                "ride456"
        );
    }

    @KafkaListener(topics = "ride.cancelled", groupId = "notification-service-group")
    public void handleRideCancelled(String message) {
        log.info("🔔 Event reçu [ride.cancelled]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Course annulée",
                "Votre course a été annulée",
                NotificationType.RIDE_CANCELLED,
                "ride456"
        );
    }

    // ============================================
    // PARTAGE DYNAMIQUE
    // ============================================

    @KafkaListener(topics = "passenger.join.request", groupId = "notification-service-group")
    public void handlePassengerJoinRequest(String message) {
        log.info("🔔 Event reçu [passenger.join.request]: {}", message);

        notificationService.sendNotification(
                "driver789",
                "Nouvelle demande",
                "Un passager souhaite rejoindre votre course. Détour: +3 min, Gain: +15 DH",
                NotificationType.NEW_PASSENGER_REQUEST,
                "ride456"
        );
    }

    @KafkaListener(topics = "passenger.accepted", groupId = "notification-service-group")
    public void handlePassengerAccepted(String message) {
        log.info("🔔 Event reçu [passenger.accepted]: {}", message);

        // Notifier le nouveau passager
        notificationService.sendNotification(
                "passenger456",
                "Demande acceptée !",
                "Le taxi arrive dans 8 minutes pour vous prendre",
                NotificationType.PASSENGER_ACCEPTED,
                "ride456"
        );
    }

    @KafkaListener(topics = "passenger.rejected", groupId = "notification-service-group")
    public void handlePassengerRejected(String message) {
        log.info("🔔 Event reçu [passenger.rejected]: {}", message);

        notificationService.sendNotification(
                "passenger456",
                "Demande refusée",
                "Le conducteur ne peut pas accepter plus de passagers",
                NotificationType.PASSENGER_REJECTED,
                "ride456"
        );
    }

    @KafkaListener(topics = "passenger.joined", groupId = "notification-service-group")
    public void handlePassengerJoined(String message) {
        log.info("🔔 Event reçu [passenger.joined]: {}", message);

        // Notifier les autres passagers
        notificationService.sendNotification(
                "passenger123",
                "Nouveau passager",
                "Un passager a rejoint votre course. Itinéraire mis à jour",
                NotificationType.PASSENGER_JOINED,
                "ride456"
        );

        // Notifier le conducteur
        notificationService.sendNotification(
                "driver789",
                "Passager monté",
                "Le nouveau passager est à bord",
                NotificationType.PASSENGER_JOINED,
                "ride456"
        );
    }

    @KafkaListener(topics = "passenger.dropped", groupId = "notification-service-group")
    public void handlePassengerDropped(String message) {
        log.info("🔔 Event reçu [passenger.dropped]: {}", message);

        notificationService.sendNotification(
                "passenger456",
                "Destination atteinte",
                "Merci d'avoir voyagé avec nous !",
                NotificationType.PASSENGER_DROPPED,
                "ride456"
        );
    }

    // ============================================
    // ITINÉRAIRE & NAVIGATION
    // ============================================

    @KafkaListener(topics = "route.updated", groupId = "notification-service-group")
    public void handleRouteUpdated(String message) {
        log.info("🔔 Event reçu [route.updated]: {}", message);

        // Notifier tous les passagers
        notificationService.sendNotification(
                "passenger123",
                "Itinéraire mis à jour",
                "Votre itinéraire a été optimisé. Nouvelle heure d'arrivée: 14h25",
                NotificationType.ROUTE_UPDATED,
                "ride456"
        );
    }

    @KafkaListener(topics = "eta.updated", groupId = "notification-service-group")
    public void handleEtaUpdated(String message) {
        log.info("🔔 Event reçu [eta.updated]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Heure d'arrivée mise à jour",
                "Arrivée estimée: 14h30 (retard de 5 min dû au trafic)",
                NotificationType.ETA_UPDATED,
                "ride456"
        );
    }

    // ============================================
    // PAIEMENT
    // ============================================

    @KafkaListener(topics = "payment.preauthorized", groupId = "notification-service-group")
    public void handlePaymentPreauthorized(String message) {
        log.info("🔔 Event reçu [payment.preauthorized]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Paiement en attente",
                "Votre paiement de 45 DH est préautorisé",
                NotificationType.PAYMENT_PREAUTHORIZED,
                "ride456"
        );
    }

    @KafkaListener(topics = "payment.captured", groupId = "notification-service-group")
    public void handlePaymentCaptured(String message) {
        log.info("🔔 Event reçu [payment.captured]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Paiement réussi",
                "Votre paiement de 45 DH a été traité avec succès",
                NotificationType.PAYMENT_CAPTURED,
                "ride456"
        );
    }

    @KafkaListener(topics = "payment.failed", groupId = "notification-service-group")
    public void handlePaymentFailed(String message) {
        log.info("🔔 Event reçu [payment.failed]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Échec du paiement",
                "Le paiement n'a pas pu être traité. Veuillez mettre à jour votre méthode de paiement",
                NotificationType.PAYMENT_FAILED,
                "ride456"
        );
    }

    @KafkaListener(topics = "payment.refunded", groupId = "notification-service-group")
    public void handlePaymentRefunded(String message) {
        log.info("🔔 Event reçu [payment.refunded]: {}", message);

        notificationService.sendNotification(
                "passenger123",
                "Remboursement traité",
                "Votre remboursement de 45 DH a été effectué",
                NotificationType.PAYMENT_REFUNDED,
                "ride456"
        );
    }
}