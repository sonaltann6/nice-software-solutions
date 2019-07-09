package com.nss.simplexweb.push.notifications.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nss.simplexweb.push.notifications.model.PushNotification;

@Repository("pushNotificationRepository")
public interface PushNotificationRepo extends JpaRepository<PushNotification, Long>{

	//find
	ArrayList<PushNotification> findAll();
		
	PushNotification findByDeviveTokenAndUserUserId(String deviceToken, Long userId);
	
	ArrayList<PushNotification> findByUserUserId(Long userId);
	
	PushNotification findByDeviveToken(String deviceToken);
}
