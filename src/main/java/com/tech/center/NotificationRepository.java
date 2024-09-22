package com.tech.center;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface NotificationRepository extends JpaRepository<Notification, Long>{

	@Query(value = "SELECT * FROM notification n WHERE n.user_id = :userId", nativeQuery = true)
    List<Notification> currentUserNotification(@Param("userId") Long userId);
	
	@Transactional
    @Modifying
	@Query(value = "DELETE FROM notification n WHERE n.user_id = :userId", nativeQuery = true)
    void deleteAllByUserId(@Param("userId") Long userId);
}
