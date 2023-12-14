package com.example.cfc.repository.postgre;

import com.example.cfc.model.postgre.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(value = "SELECT n.user_id_tg FROM Notification n WHERE (n.morning_notification = :notificationType OR n.evening_notification = :notificationType) AND n.notification_on = true",
            nativeQuery = true)
    ArrayList<Long> findUserIdsByNotificationType(@Param("notificationType") String notificationType);

    @Query(value = "SELECT * FROM Notification n WHERE n.user_id_tg = :userIdTg", nativeQuery = true)
    Optional<Notification> findNotificationByUserIDTg(@Param("userIdTg") Long userIdTg);

}
