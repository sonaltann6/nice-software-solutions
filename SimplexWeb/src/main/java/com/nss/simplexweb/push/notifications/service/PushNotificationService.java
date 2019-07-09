package com.nss.simplexweb.push.notifications.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nss.simplexrest.push.notifications.apns.APNS;
import com.nss.simplexrest.push.notifications.apns.ApnsService;
import com.nss.simplexweb.enums.DEVICE;
import com.nss.simplexweb.enums.NOTIFICATION;
import com.nss.simplexweb.notifications.model.NotificationParentEntityTable;
import com.nss.simplexweb.notifications.repository.NotificationParentEntityRepo;
import com.nss.simplexweb.push.notifications.model.PushNotification;
import com.nss.simplexweb.push.notifications.repository.PushNotificationRepo;

@Service("pushNotificationService")
public class PushNotificationService {

	@Autowired
	PushNotificationRepo pushNotificationRepo;
	
	@Autowired
	NotificationParentEntityRepo entityRepo;
	
	@Value("${file.certificate.notify}")
	private String CERTIFICATE_FOLDER_PATH;

	@Value("${certificate.pwd}")
	private String CERTIFICATE_PWD;

	@Value("${certificate.name}")
	private String CERTIFICATE_NAME;
	
	@Value("${push.notif.server.key}")
	private String SERVER_KEY;
	
	//send push notification
	public Map<String, Object> sendPushNotification(PushNotification pushNotification, long notifcationType) throws IOException {
		NotificationParentEntityTable entityTable = entityRepo.findByNotificationParentId(notifcationType);
		Map<String, Object> map = new HashMap<String, Object>();
		if (pushNotification.getDevicePlatform().equalsIgnoreCase(DEVICE.IOS.name())) {
			System.out.println(CERTIFICATE_FOLDER_PATH);
			System.out.println(CERTIFICATE_PWD);
			InputStream stream = new FileInputStream(CERTIFICATE_FOLDER_PATH + File.separator + CERTIFICATE_NAME);
			ApnsService service = APNS.newService()
					.withCert(stream, CERTIFICATE_PWD)
					.withSandboxDestination()
					.build();

			String payload = APNS.newPayload()
					.alertTitle(entityTable.getNotificationParentTypeName())
					.alertBody(entityTable.getNotificationTitle())
					.sound("default")
					.build();

			service.push(pushNotification.getDeviveToken(), payload);
			
		}else if(pushNotification.getDevicePlatform().equalsIgnoreCase(DEVICE.ANDROID.name())) {
			System.out.println(SERVER_KEY);
			String pushMessage = "{\"data\":{\"title\":\"" +
					entityTable.getNotificationParentTypeName()  +
	                "\",\"message\":\"" +
	                entityTable.getNotificationTitle() +
	                "\"},\"to\":\"" +
	                pushNotification.getDeviveToken() +
	                "\"}";
	        // Create connection to send FCM Message request.
	        URL url = new URL("https://fcm.googleapis.com/fcm/send");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
	        conn.setRequestProperty("Content-Type", "application/json");
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);

	        // Send FCM message content.
	        OutputStream outputStream = conn.getOutputStream();
	        outputStream.write(pushMessage.getBytes());

	        System.out.println(conn.getResponseCode());
	        System.out.println(conn.getResponseMessage());
		}
		System.out.println("The message has been hopefully sent...");
		map.put(NOTIFICATION.NOTIFICATION_LIST.name(), entityTable);
		map.put(NOTIFICATION.PUSH_NOTIFICATION.name(), pushNotification);
		return map;
	}
	
	public PushNotification doesDeviceTokenExist(String deviceToken) {
		PushNotification pushNotification = pushNotificationRepo.findByDeviveToken(deviceToken);
		return pushNotification;
	}
}
