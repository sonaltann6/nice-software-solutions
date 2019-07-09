package com.nss.simplexweb.push.notifications.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.nss.simplexweb.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the push notification database table.
 * 
 */
@Data
@Builder
@Entity
@AllArgsConstructor 
@NoArgsConstructor
@Table(name="push_notifications_tbl")
public class PushNotification implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="push_notification_id")
	private Long pushNotificationId;
	
	@Column(name="device_token", unique=true)
	private String deviveToken;
	
	@Column(name="device_platform")
	private String devicePlatform;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;		
}
