package com.nss.simplexweb.notifications.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nss.simplexweb.notifications.model.NotificationsTbl;

@Repository("notificationRepository")
public interface NotificationRepository extends JpaRepository<NotificationsTbl, Long>{

	//find
	ArrayList<NotificationsTbl> findAll();
	
	NotificationsTbl findByNotificationId(Long notificationId);
	
	ArrayList<NotificationsTbl> findByIsReadFalse();
	
	ArrayList<NotificationsTbl> findByUserUserIdAndIsReadFalse(Long userId);

}
