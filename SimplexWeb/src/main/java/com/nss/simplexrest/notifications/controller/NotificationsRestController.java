package com.nss.simplexrest.notifications.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nss.simplexweb.enums.NOTIFICATION;
import com.nss.simplexweb.notifications.model.NotificationsTbl;
import com.nss.simplexweb.notifications.repository.NotificationRepository;
import com.nss.simplexweb.notifications.service.NotificationService;
import com.nss.simplexweb.user.service.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/rest/notification")
@Api(value = "Notification Resource REST Endpoint", description = "All Notification Related Operations")
public class NotificationsRestController {

	@Autowired
	NotificationRepository notificationRepo;
	
	@Autowired
	UserService userService;
	
	@Autowired
	NotificationService notificationService;
	
	@GetMapping(value = "/getNotificationsByGroup")
	@ResponseBody
	public Map<String, List<?>> getNotificationsByGroupForUser(@RequestParam("userId") Long userId) throws JsonProcessingException{
		//find by group
		Map<String, List<?>> map = notificationService.getNotificationListByGroup(userId); 
		return map;		
	}
	
	@PostMapping(value = "/getReadNotification")
	@ResponseBody
	public Map<String, Object> getReadNotification(@RequestParam("notificationId") Long notificationId){
		Map<String, Object> map = new HashMap<String, Object>();
		NotificationsTbl notification = notificationService.readNotification(notificationId);
		map.put(NOTIFICATION.NOTIFICATION_LIST.name(), notification);
		return map;
	}
}
