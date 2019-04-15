package com.nss.simplexrest.push.notifications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.push.notifications.model.PushNotification;
import com.nss.simplexweb.push.notifications.repository.PushNotificationRepo;
import com.nss.simplexweb.push.notifications.service.PushNotificationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/push/notifications")
@Api(value = "Push notifications Resource REST Endpoint", description = "All Push notifications Related Operations")
public class PushNotificationsRestController {

	@Autowired
	PushNotificationService pushNotificationService;
	
	@Autowired
	PushNotificationRepo pushNotificationRepo;
	
	
	@PostMapping(value = "/getDeviceToken")
	public void getDeviceToken(@RequestBody PushNotification pushNotification) {
		System.out.println("Device token: " +pushNotification.getDeviveToken());
		System.out.println("Device platform: " +pushNotification.getDevicePlatform());
		System.out.println("User: " +pushNotification.getUser().getUserId());
		pushNotificationRepo.save(pushNotification);
	}
}
