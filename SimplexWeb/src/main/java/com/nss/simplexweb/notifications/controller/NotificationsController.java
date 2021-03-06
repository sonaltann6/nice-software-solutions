package com.nss.simplexweb.notifications.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.nss.simplexweb.enums.NOTIFICATION;
import com.nss.simplexweb.notifications.model.NotificationsTbl;
import com.nss.simplexweb.notifications.repository.NotificationRepository;
import com.nss.simplexweb.notifications.service.NotificationService;

@Controller
@RequestMapping("/notification/notificationsController")
public class NotificationsController {
	
	@Autowired
	NotificationRepository notificationRepo;
	
	@Autowired
	NotificationService notificationService;

	/*@RequestMapping(value = "/getRegistrationNotification", method = RequestMethod.GET)
	public Map<String, List<?>> getRegistrationNotifiaction(@RequestParam("userId") Long userId){
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		List<NotificationsTbl> notificationList = notificationRepo.findByIsReadFalse();
		map.put(NOTIFICATION.REGISTRATION_NOTIFICATION.name(), notificationList);
		System.out.println(map.toString());
		return map;		
	}*/
	
	@RequestMapping(value = "/getReadNotification", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getReadNotification(@RequestParam("notificationId") Long notificationId){
		Map<String, Object> map = new HashMap<String, Object>();
		NotificationsTbl notification = notificationService.readNotification(notificationId);
		map.put(NOTIFICATION.NOTIFICATION_LIST.name(), notification);
		return map;
	}
}
