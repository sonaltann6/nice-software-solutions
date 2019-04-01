package com.nss.simplexweb.notifications.model;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * The persistent class for the notification_parent_entity_table database table.
 * 
 */
@Data
@Builder
@Entity
@AllArgsConstructor 
@NoArgsConstructor
@Table(name="notification_parent_entity_table")
public class NotificationParentEntityTable implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="notification_parent_id")
	private Long notificationParentId;

	@Column(name="notification_parent_type_abbr")
	private String notificationParentTypeAbbr;

	@Column(name="notification_parent_type_name")
	private String notificationParentTypeName;
	
	@Column(name="on_click_url")
	private String onClickUrl;
	
	@Column(name="notification_desc")
	private String notificationDesc;

	@Column(name="notification_icon")
	private String notificationIcon;
	
	@Column(name="notification_title")
	private String notificationTitle;
}