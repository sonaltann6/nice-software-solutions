package com.nss.simplexweb.notifications.model;

import java.io.Serializable;
import javax.persistence.*;

import com.nss.simplexweb.user.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
	private Long notificationId;

	@Column(name="is_read")
	private boolean isRead;

	@Column(name="notification_parent_entity_id")
	private Long notificationParentEntityId;

	@ManyToOne
	@JoinColumn(name="notification_parent_entity_type_id")
	private NotificationParentEntityTable entityTable;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
}