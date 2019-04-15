package com.nss.simplexweb.enquiry.template.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nss.simplexweb.SessionUtility;
import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enquiry.template.service.EnquiryTemplateService;
import com.nss.simplexweb.enquiry.template.service.TemplatePresetsService;
import com.nss.simplexweb.enums.ENQUIRY;
import com.nss.simplexweb.enums.MAIL;
import com.nss.simplexweb.notifications.service.NotificationService;
import com.nss.simplexweb.push.notifications.model.PushNotification;
import com.nss.simplexweb.push.notifications.repository.PushNotificationRepo;
import com.nss.simplexweb.push.notifications.service.PushNotificationService;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.utility.mail.MailBean;



@Controller
@RequestMapping(value={"/enquiry/globalTemplateController"})
public class GlobalTemplateController {
	
	@Autowired
	private TemplatePresetsService templatePresetsService;
	
	@Autowired
	private EnquiryTemplateService enquiryTemplateService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	PushNotificationService pushNotificationService;
	
	@Autowired
	PushNotificationRepo pushNotificationRepo;

	@RequestMapping(value = "/getGlobalTemplate", method = RequestMethod.GET)
	public ModelAndView getGlobalTemplate() {
		ModelAndView mav = new ModelAndView();
		mav
			.addObject(ENQUIRY.ENQUIRY, new EnquiryTemplateBean())
			.addObject(ENQUIRY.ALL_PRESETS, templatePresetsService.getAllPresets())
			.setViewName("enquiry/templates/GlobalTemplate");
		return mav;
	}
	
	@RequestMapping(value = "/saveGlobalTemplateDetails", method = RequestMethod.POST)
	public ModelAndView saveGlobalTemplateDetails(EnquiryTemplateBean enquiryBean, HttpServletRequest request) throws IOException {
		enquiryBean.setRequester(SessionUtility.getUserFromSession(request));
		if(enquiryBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_4PANEL)) {
			enquiryTemplateService.performAll4PanelCalculations(enquiryBean);
		}
		if(enquiryBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_Q_BAFFLE)) {
			enquiryTemplateService.performAllBaffleCalculations(enquiryBean);
		}
		if(enquiryBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_CONTAINER_LINER)) {
			enquiryTemplateService.performAllContainerLinerCalculations(enquiryBean);
		}
		if(enquiryBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_HOOD)) {
			enquiryTemplateService.performAllHoodBagCalculations(enquiryBean);
		}
		if(enquiryBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_PLATEN)) {
			enquiryTemplateService.performAllPlatenBagCalculations(enquiryBean);
		}
		if(enquiryBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_SINGLE_LOOP)) {
			enquiryTemplateService.performAllSingleLoopCalculations(enquiryBean);
		}
		if(enquiryBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_TUBULAR_CIRCULAR)) {
			enquiryTemplateService.performAllTubularCalculations(enquiryBean);
		}
		if(enquiryBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_TWO_LOOP)) {
			enquiryTemplateService.performAllTwoLoopCalculations(enquiryBean);
		}
		if(enquiryBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_UPANEL)) {
			enquiryTemplateService.performAllUpanelCalculations(enquiryBean);
		}
		if(enquiryBean != null) {
			enquiryBean = enquiryTemplateService.saveNewEnquiry(enquiryBean);
		}
		notificationService.saveNewEnquiryNotification(enquiryBean, 2);
		PushNotification pushNotification = pushNotificationRepo.findByUserUserId(enquiryBean.getRequester().getUserId());
		pushNotificationService.sendPushNotification(pushNotification,2);
		return new ModelAndView("redirect:" + "/enquiry/globalTemplateController/getGlobalTemplateQuotation?enquiryId="+enquiryBean.getEnquiryId()+"&enquiryNumber="+enquiryBean.getEnquiryNumber());
	}
	
	@RequestMapping(value = "/getGlobalTemplateQuotation", method = RequestMethod.GET)
	//Intentionally two params required, so that end user can find the quotation by sending exactly two params combination which is hard to guess 
	public ModelAndView getGlobalTemplateQuotation(@RequestParam("enquiryId")Long enquiryId, @RequestParam("enquiryNumber")String enquiryNumber) {
		ModelAndView mav = new ModelAndView();
		EnquiryTemplateBean enquiryTemplateBean = enquiryTemplateService.getEnquiryDetailsByEnquiryIdAndEnquiryNumber(enquiryId, enquiryNumber);
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
	
	@RequestMapping(value = "/emailGlobalTemplateQuotation", method = RequestMethod.POST)
	//Intentionally two params required, so that end user can find the quotation by sending exactly two params combination which is hard to guess 
	@ResponseBody
	public String emailGlobalTemplateQuotation(@RequestParam("enquiryId")Long enquiryId, @RequestParam("enquiryNumber")String enquiryNumber, MailBean mailBean, HttpServletResponse response) {
		return enquiryTemplateService.emailGlobalTemplateEnquiryQuotation(enquiryId, enquiryNumber, mailBean);
	}
	
	@RequestMapping(value = "/downloadGlobalTemplateQuotation", method = RequestMethod.GET)
	//Intentionally two params required, so that end user can find the quotation by sending exactly two params combination which is hard to guess 
	public void downloadGlobalTemplateQuotation(@RequestParam("enquiryId")Long enquiryId, @RequestParam("enquiryNumber")String enquiryNumber, HttpServletResponse response) {
		enquiryTemplateService.downloadGlobalTemplateEnquiryQuotation(enquiryId, enquiryNumber, response);
	}
	
	@RequestMapping(value = "/getMyEnquiryHistory", method = RequestMethod.GET) 
	public ModelAndView getMyEnquiryHistory(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		User user = SessionUtility.getUserFromSession(request);
		ArrayList<EnquiryTemplateBean> enquiryHistoryList = enquiryTemplateService.getEnquiryHistoryForUser(user);
		
		mav
			.addObject(ENQUIRY.ENQUIRY_HISTORY_LIST, enquiryHistoryList)
			.setViewName("enquiry/my_enquiry_history");
		return mav;
	}
	
	@RequestMapping(value = "/getEnquiryBean", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getEnquiryBean(@RequestParam("enquiryId") Long enquiryId){
		Map<String, Object> map = new HashMap<String, Object>();
		EnquiryTemplateBean enquiryTemplateBean = enquiryTemplateService.getEnquiryDetailsByEnquiryId(enquiryId);
		map.put(ENQUIRY.ENQUIRY_HISTORY_LIST, enquiryTemplateBean);
		return map;
	}
}
