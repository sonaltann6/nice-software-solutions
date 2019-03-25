package com.nss.simplexrest.enquiry.template.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enquiry.template.service.EnquiryTemplateService;
import com.nss.simplexweb.enquiry.template.service.TemplatePresetsService;
import com.nss.simplexweb.enums.ENQUIRY;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.UserService;
import com.nss.simplexweb.utility.mail.MailBean;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/enquiry")
@Api(value = "Enquiry Resource REST Endpoint", description = "All Enquiry Related Operations")
public class GlobalTemplateRestController {
	
	@Autowired
	private TemplatePresetsService templatePresetsService;
	
	@Autowired
	private EnquiryTemplateService enquiryTemplateService;
	
	@Autowired
	private UserService userService;
	
	@GetMapping(value = "/getGlobalTemplate")
	public Map<String, List<?>> getGlobalTemplate() {
		return templatePresetsService.getAllPresets();
	}
	
	@PostMapping(value = "/saveGlobalTemplateDetails")
	public EnquiryTemplateBean saveGlobalTemplateDetails(@RequestBody EnquiryTemplateBean enquiryTemplateBean) {
		//enquiryTemplateBean.setRequester(user);
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_4PANEL)) {
			enquiryTemplateService.performAll4PanelCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_Q_BAFFLE)) {
			enquiryTemplateService.performAllBaffleCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_CONTAINER_LINER)) {
			enquiryTemplateService.performAllContainerLinerCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_HOOD)) {
			enquiryTemplateService.performAllHoodBagCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_PLATEN)) {
			enquiryTemplateService.performAllPlatenBagCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_SINGLE_LOOP)) {
			enquiryTemplateService.performAllSingleLoopCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_TUBULAR_CIRCULAR)) {
			enquiryTemplateService.performAllTubularCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_TWO_LOOP)) {
			enquiryTemplateService.performAllTwoLoopCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_UPANEL)) {
			enquiryTemplateService.performAllUpanelCalculations(enquiryTemplateBean);
		}
		if(enquiryTemplateBean != null) {
			enquiryTemplateService.saveNewEnquiry(enquiryTemplateBean);
		}		
		return enquiryTemplateBean;
	}
	
	@PostMapping(value = "/getGlobalTemplateQuotation")
	public EnquiryTemplateBean getGlobalTemplateQuotation(@RequestParam("enquiryId")Long enquiryId, @RequestParam("enquiryNumber")String enquiryNumber) {
		EnquiryTemplateBean enquiryTemplateBean = enquiryTemplateService.getEnquiryDetailsByEnquiryIdAndEnquiryNumber(enquiryId, enquiryNumber);
	
		return enquiryTemplateBean;
	}
	
	@GetMapping(value = "/getMyEnquiryHistory")
	public ArrayList<EnquiryTemplateBean> getMyEnquiryHistory(@RequestParam ("userId") Long userId){
		User currentUser = userService.findUserByUserId(userId);
		return enquiryTemplateService.getEnquiryHistoryForUser(currentUser);
	}
	
	@PostMapping(value = "/emailGlobalTemplateQuotation")
	public String emailGlobalTemplateQuotation(@RequestParam("enquiryId")Long enquiryId, @RequestParam("enquiryNumber")String enquiryNumber, MailBean mailBean) {
		return enquiryTemplateService.emailGlobalTemplateEnquiryQuotation(enquiryId, enquiryNumber, mailBean);
	}
	
	@GetMapping(value = "/downloadGlobalTemplateQuotation")
	public void downloadGlobalTemplateQuotation(@RequestParam("enquiryId")Long enquiryId, @RequestParam("enquiryNumber")String enquiryNumber, HttpServletResponse response) {
		enquiryTemplateService.downloadGlobalTemplateEnquiryQuotation(enquiryId, enquiryNumber, response);
	}
	
	@GetMapping(value = "/getEnquiryBean")
	public Map<String, Object> getEnquiryBean(@RequestParam("enquiryId") Long enquiryId){
		Map<String, Object> map = new HashMap<String, Object>();
		EnquiryTemplateBean enquiryTemplateBean = enquiryTemplateService.getEnquiryDetailsByEnquiryId(enquiryId);
		map.put(ENQUIRY.ENQUIRY_HISTORY_LIST, enquiryTemplateBean);
		return map;
	}
}
