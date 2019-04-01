package com.nss.simplexweb.notifications.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enums.NOTIFICATION;
import com.nss.simplexweb.enums.PROJECT;
import com.nss.simplexweb.notifications.model.NotificationParentEntityTable;
import com.nss.simplexweb.notifications.model.NotificationsTbl;
import com.nss.simplexweb.notifications.repository.NotificationParentEntityRepo;
import com.nss.simplexweb.notifications.repository.NotificationRepository;
import com.nss.simplexweb.po.model.PODetail;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.utility.document.model.Document;

@Service("notificationService")
public class NotificationService {

	@Autowired
	NotificationParentEntityRepo entityRepo;
	
	@Autowired
	NotificationRepository notificationRepository;
	
	//save new registration notification
	public String saveNewRegistrationNotification(User user, long id) {
		NotificationsTbl tbl = new NotificationsTbl();
		tbl.setEntityTable(entityRepo.findByNotificationParentId(id));
		tbl.setNotificationParentEntityId(user.getUserId());
		tbl.setUser(user);
		notificationRepository.save(tbl);
		return PROJECT.SUCCESS_MSG.name();
	}
	
	//save new enquiry
	public String saveNewEnquiryNotification(EnquiryTemplateBean enquiryTemplateBean, long id) {
		NotificationsTbl tbl = new NotificationsTbl();
		tbl.setEntityTable(entityRepo.findByNotificationParentId(id));
		tbl.setNotificationParentEntityId(enquiryTemplateBean.getEnquiryId());
		tbl.setUser(enquiryTemplateBean.getRequester());
		notificationRepository.save(tbl);
		return PROJECT.SUCCESS_MSG.name();
	}
	
	//save new po
	public String saveNewPONotification(PODetail poDetail, long id) {
		NotificationsTbl tbl = new NotificationsTbl();
		tbl.setEntityTable(entityRepo.findByNotificationParentId(id));
		tbl.setNotificationParentEntityId(poDetail.getPoId());
		tbl.setUser(poDetail.getRequester());
		notificationRepository.save(tbl);
		return PROJECT.SUCCESS_MSG.name();
	}
	
	//save new documents
	public String saveNewDocumentNotification(Document document, long id) {
		NotificationsTbl tbl = new NotificationsTbl();
		tbl.setEntityTable(entityRepo.findByNotificationParentId(id));
		tbl.setNotificationParentEntityId(document.getDocumentDetailId());
		tbl.setUser(document.getDocumentOwnerPartner());
		notificationRepository.save(tbl);
		return PROJECT.SUCCESS_MSG.name();
	}

	public ArrayList<List<?>> getNotificationByGroup(Long userId) throws JsonProcessingException {
		ArrayList<List<?>> list = new ArrayList<List<?>>();
		ArrayList<NotificationParentEntityTable> notificationParentList = entityRepo.findAll();
		ArrayList<NotificationsTbl> notificationList = notificationRepository.findByUserUserIdAndIsReadFalse(userId);
		ArrayList<NotificationsTbl> registrationNotificationList = new ArrayList<>();
		ArrayList<NotificationsTbl> enquiryNotificationList = new ArrayList<>();
		ArrayList<NotificationsTbl> poNotificationList = new ArrayList<>();
		ArrayList<NotificationsTbl> documentNotificationList = new ArrayList<>();
		
		//for loop to find list
		for(NotificationParentEntityTable entityTable : notificationParentList) {
			for(NotificationsTbl notifications : notificationList) {
				if(entityTable.getNotificationParentId().equals(notifications.getEntityTable().getNotificationParentId())) {
					if(notifications.getEntityTable().getNotificationParentTypeAbbr().equals(NOTIFICATION.REGISTRATION.name())) {
						registrationNotificationList.add(notifications);	
					}
					if(notifications.getEntityTable().getNotificationParentTypeAbbr().equals(NOTIFICATION.NEW_ENQUIRY.name())) {
						enquiryNotificationList.add(notifications);
					}
					if(notifications.getEntityTable().getNotificationParentTypeAbbr().equals(NOTIFICATION.PO.name())) {
						poNotificationList.add(notifications);
					}
					if(notifications.getEntityTable().getNotificationParentTypeAbbr().equals(NOTIFICATION.DOCUMENTS.name())) {
						documentNotificationList.add(notifications);
					}
				}
			}
		}
		list.add(registrationNotificationList);
		list.add(enquiryNotificationList);
		list.add(poNotificationList);
		list.add(documentNotificationList);
		return list;
	}
	
	public void readNotification(Long readNotification) {
		NotificationsTbl notification = notificationRepository.findByNotificationId(readNotification);
		notification.setRead(true);
		notificationRepository.save(notification);
	}
}


