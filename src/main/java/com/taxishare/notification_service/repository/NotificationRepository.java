package com.taxishare.notification_service.repository;

import com.taxishare.notification_service.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(String userId);

    List<Notification> findByUserIdAndIsReadFalse(String userId);

    Long countByUserIdAndIsReadFalse(String userId);
}