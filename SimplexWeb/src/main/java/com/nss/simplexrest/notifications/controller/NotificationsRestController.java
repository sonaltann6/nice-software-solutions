package com.nss.simplexrest.notifications.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	static
	NotificationService notificationService;

	@GetMapping(value = "/getRegistrationNotification")
	public Map<String, List<?>> getRegistrationNotifiaction(@RequestParam("userId") Long userId){
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		
		//find by userid 
		List<NotificationsTbl> notificationList = notificationRepo.findByUserUserIdAndIsReadFalse(userId);
		map.put(NOTIFICATION.REGISTRATION_NOTIFICATION.name(), notificationList);
		System.out.println(map.toString());
		return map;		
	}
	
	@GetMapping(value = "/getNotificationsByGroup")
	public @ResponseBody ArrayList<List<?>> getNotificationsByGroupForUser(@RequestParam("userId") Long userId){
		//find by group
		ArrayList<List<?>> list = new ArrayList<List<?>>();
		try {
			list = notificationService.getNotificationByGroup(userId);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;		
	}
	
	/*static ArrayList<List<?>> list = new ArrayList<List<?>>();
	static {
		try {
			list = notificationService.getNotificationByGroup();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping(value = "/getStaticNotification")
	public ArrayList<List<?>> getStaticNotification(){
		return list;
	}*/
}
