package com.taxishare.notification_service.controller;

import com.taxishare.notification_service.model.Notification;
import com.taxishare.notification_service.model.NotificationType;
import com.taxishare.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Notification>> getUserNotifications(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getUserNotifications(userId));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(@PathVariable String userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(userId));
    }

    @GetMapping("/user/{userId}/count-unread")
    public ResponseEntity<Map<String, Long>> countUnread(@PathVariable String userId) {
        return ResponseEntity.ok(Map.of("unreadCount", notificationService.countUnread(userId)));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok().build();
    }

    // Endpoint pour tests manuels
    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(@RequestBody Map<String, String> request) {
        Notification notification = notificationService.sendNotification(
                request.get("userId"),
                request.get("title"),
                request.get("message"),
                NotificationType.valueOf(request.get("type")),
                request.get("rideId")
        );
        return ResponseEntity.ok(notification);
    }
}
