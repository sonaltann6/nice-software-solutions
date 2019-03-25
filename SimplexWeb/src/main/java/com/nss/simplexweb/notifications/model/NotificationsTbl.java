package com.nss.simplexweb.notifications.model;

import java.io.Serializable;
import javax.persistence.*;

import com.nss.simplexweb.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;


/**
 * The persistent class for the notifications_tbl database table.
 * 
 */
@Data
@Builder
@Entity
@AllArgsConstructor 
@NoArgsConstructor
@Table(name="notifications_tbl")
public class NotificationsTbl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="notification_id")
	private String notificationId;

	private byte isRead;

	@Column(name="notification_desc")
	private String notificationDesc;

	@Column(name="notification_icon")
	private String notificationIcon;

	@Column(name="notification_parent_entity_id")
	private BigInteger notificationParentEntityId;

	@ManyToOne
	@JoinColumn(name="notification_parent_entity_type_id")
	private NotificationParentEntityTable entityTable;

	@Column(name="notification_title")
	private String notificationTitle;

	@Column(name="on_click_url")
	private String onClickUrl;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
}