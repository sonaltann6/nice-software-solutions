package com.nss.simplexrest.enquiry.template.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.enquiry.abc.template.service.EnquiryABCTemplateService;
import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enquiry.template.service.TemplatePresetsService;
import com.nss.simplexweb.enums.ENQUIRY;
import com.nss.simplexweb.notifications.service.NotificationService;
import com.nss.simplexweb.push.notifications.model.PushNotification;
import com.nss.simplexweb.push.notifications.repository.PushNotificationRepo;
import com.nss.simplexweb.push.notifications.service.PushNotificationService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/enquiry")
@Api(value = "Enquiry Resource REST Endpoint", description = "All Enquiry Related Operations")
public class ABCTemplateRestController {

	@Autowired
	private TemplatePresetsService templatePresetsService;
	
	@Autowired
	private EnquiryABCTemplateService enquiryAbcTemplateService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	PushNotificationRepo pushNotificationRepo;
	
	@Autowired
	PushNotificationService pushNotificationService;
	
	
	@GetMapping(value = "/getABCTemplate")
	public Map<String, List<?>> getGlobalTemplate() {
		return templatePresetsService.getAllABCPresets();
	}
	
	@PostMapping(value = "/saveABCTemplateDetails")
	public EnquiryTemplateBean saveABCTemplateDetails(@RequestBody EnquiryTemplateBean enquiryTemplateBean) throws IOException {
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_4PANEL)) {
			enquiryAbcTemplateService.performAll4PanelCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_Q_BAFFLE)) {
			enquiryAbcTemplateService.performAllBaffleCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_SINGLE_LOOP)) {
			enquiryAbcTemplateService.performAllSingleLoopCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_TUBULAR_CIRCULAR)) {
			enquiryAbcTemplateService.performAllTubularCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_TWO_LOOP)) {
			enquiryAbcTemplateService.performAllTwoLoopCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_UPANEL)) {
			enquiryAbcTemplateService.performAllUpanelCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean != null) {
			enquiryAbcTemplateService.saveNewEnquiry(enquiryTemplateBean);
		}
		notificationService.saveNewEnquiryNotification(enquiryTemplateBean, 2);
		ArrayList<PushNotification> listPushNotification = pushNotificationRepo.findByUserUserId(enquiryTemplateBean.getRequester().getUserId());
		for(PushNotification pushNotification : listPushNotification) {
			pushNotificationService.sendPushNotification(pushNotification,2);
		}
		return enquiryTemplateBean;
	}
}
