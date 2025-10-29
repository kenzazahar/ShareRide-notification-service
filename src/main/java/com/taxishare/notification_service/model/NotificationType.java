package com.taxishare.notification_service.model;

public enum NotificationType {
    // ============================================
    // CYCLE DE VIE DE LA COURSE
    // ============================================
    RIDE_CREATED,           // Course créée
    RIDE_ACCEPTED,          // Course acceptée par conducteur
    DRIVER_ARRIVING,        // Conducteur en route
    DRIVER_ARRIVED,         // Conducteur arrivé
    RIDE_STARTED,           // Course démarrée
    RIDE_COMPLETED,         // Course terminée
    RIDE_CANCELLED,         // Course annulée

    // ============================================
    // PARTAGE DYNAMIQUE
    // ============================================
    NEW_PASSENGER_REQUEST,  // Nouveau passager veut rejoindre
    PASSENGER_ACCEPTED,     // Passager accepté par conducteur
    PASSENGER_REJECTED,     // Passager refusé
    PASSENGER_JOINED,       // Passager monté dans le taxi
    PASSENGER_DROPPED,      // Passager déposé

    // ============================================
    // ITINÉRAIRE & NAVIGATION
    // ============================================
    ROUTE_UPDATED,          // Itinéraire mis à jour
    ETA_UPDATED,            // Temps d'arrivée estimé mis à jour

    // ============================================
    // PAIEMENT
    // ============================================
    PAYMENT_PREAUTHORIZED,  // Paiement préautorisé
    PAYMENT_CAPTURED,       // Paiement effectué
    PAYMENT_FAILED,         // Paiement échoué
    PAYMENT_REFUNDED,       // Remboursement traité

    // ============================================
    // ÉVALUATIONS
    // ============================================
    RATING_REQUEST,         // Demande d'évaluation
    RATING_RECEIVED,        // Nouvelle évaluation reçue
    DRIVER_SCORE_UPDATED,   // Score conducteur mis à jour

    // ============================================
    // SYSTÈME & ADMIN
    // ============================================
    SYSTEM_ALERT,           // Alerte système
    DRIVER_VERIFIED,        // Conducteur vérifié
    USER_UPDATED            // Profil utilisateur mis à jour
}