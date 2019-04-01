package com.nss.simplexweb.notifications.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nss.simplexweb.notifications.model.NotificationParentEntityTable;

@Repository("notificationParentEntityRepository")
public interface NotificationParentEntityRepo extends JpaRepository<NotificationParentEntityTable, Long>{

	//find
	NotificationParentEntityTable findByNotificationParentId(Long id);
	
	ArrayList<NotificationParentEntityTable> findAll();
}
