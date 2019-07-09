package com.nss.simplexweb.enquiry.template.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nss.simplexweb.SessionUtility;
import com.nss.simplexweb.enquiry.abc.template.service.EnquiryABCTemplateService;
import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enquiry.template.service.TemplatePresetsService;
import com.nss.simplexweb.enums.ENQUIRY;
import com.nss.simplexweb.enums.MAIL;
import com.nss.simplexweb.notifications.service.NotificationService;
import com.nss.simplexweb.push.notifications.model.PushNotification;
import com.nss.simplexweb.push.notifications.repository.PushNotificationRepo;
import com.nss.simplexweb.push.notifications.service.PushNotificationService;
import com.nss.simplexweb.utility.mail.MailBean;

@Controller
@RequestMapping(value={"/enquiry/ABCTemplateController"})
public class ABCTemplateController {

	@Autowired
	private TemplatePresetsService templatePresetsService;
	
	@Autowired
	private EnquiryABCTemplateService enquiryAbcTemplateService;

	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	PushNotificationService pushNotificationService;
	
	@Autowired
	PushNotificationRepo pushNotificationRepo;
	
	@RequestMapping(value = "/getABCTemplate", method = RequestMethod.GET)
	public ModelAndView getEmployeeList() {
		ModelAndView mav = new ModelAndView();
		mav
			.addObject(ENQUIRY.ENQUIRY, new EnquiryTemplateBean())
			.addObject(ENQUIRY.ALL_PRESETS, templatePresetsService.getAllABCPresets())
			.setViewName("enquiry/templates/ABCTemplate");
		return mav;
	}
	
	@RequestMapping(value = "/saveABCTemplateDetails", method = RequestMethod.POST)
	public ModelAndView saveABCTemplateDetails(EnquiryTemplateBean enquiryTemplateBean, HttpServletRequest request) throws IOException {
		enquiryTemplateBean.setRequester(SessionUtility.getUserFromSession(request));
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
			enquiryTemplateBean = enquiryAbcTemplateService.saveNewEnquiry(enquiryTemplateBean);
		}
		notificationService.saveNewEnquiryNotification(enquiryTemplateBean, 2);
		ArrayList<PushNotification> listPushNotification = pushNotificationRepo.findByUserUserId(enquiryTemplateBean.getRequester().getUserId());
		for(PushNotification pushNotification : listPushNotification) {
			pushNotificationService.sendPushNotification(pushNotification,2);
		}
		return new ModelAndView("redirect:" + "/enquiry/globalTemplateController/getGlobalTemplateQuotation?enquiryId="+enquiryTemplateBean.getEnquiryId()+"&enquiryNumber="+enquiryTemplateBean.getEnquiryNumber());
	}
	
	@RequestMapping(value = "/getGlobalTemplateQuotation", method = RequestMethod.GET)
	//Intentionally two params required, so that end user can find the quotation by sending exactly two params combination which is hard to guess 
	public ModelAndView getGlobalTemplateQuotation(@RequestParam("enquiryId")Long enquiryId, @RequestParam("enquiryNumber")String enquiryNumber) {
		ModelAndView mav = new ModelAndView();
		EnquiryTemplateBean enquiryTemplateBean = enquiryAbcTemplateService.getEnquiryDetailsByEnquiryIdAndEnquiryNumber(enquiryId, enquiryNumber);
		if(enquiryTemplateBean != null) {
			mav
				.addObject(ENQUIRY.ENQUIRY, enquiryTemplateBean)
				.addObject(MAIL.MAIL.name(), new MailBean())
				.setViewName("enquiry/quotation/GlobalTemplateQuotation");
		}else {
			mav
				.addObject(ENQUIRY.ENQUIRY, new EnquiryTemplateBean())
				.addObject(ENQUIRY.ALL_PRESETS, templatePresetsService.getAllPresets())
				.setViewName("enquiry/templates/GlobalTemplate");
		}
		return mav;
	}
}
