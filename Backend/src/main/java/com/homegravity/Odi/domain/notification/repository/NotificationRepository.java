package com.homegravity.Odi.domain.notification.repository;

import com.homegravity.Odi.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<List<Notification>> findAllByReceiverIdAndDeletedAtIsNull(Long ReceiverId);

    Optional<Notification> findByIdAndDeletedAtIsNull(Long notificationId);
}
