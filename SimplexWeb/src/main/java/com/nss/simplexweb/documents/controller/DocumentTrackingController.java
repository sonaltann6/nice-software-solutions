package com.nss.simplexweb.documents.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.nss.simplexweb.SessionUtility;
import com.nss.simplexweb.documents.model.DocumentTrackStatus;
import com.nss.simplexweb.documents.model.DocumentTrackingDetail;
import com.nss.simplexweb.documents.model.DocumentType;
import com.nss.simplexweb.documents.service.DocumentTrackingService;
import com.nss.simplexweb.enums.DISTRIBUTER;
import com.nss.simplexweb.enums.DOCUMENT;
import com.nss.simplexweb.enums.PROJECT;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.DistributerService;

@Controller
@RequestMapping(value={"/documents/documentTrackingController"})
public class DocumentTrackingController {

	@Autowired
	private DocumentTrackingService documentTrackingService;
	
	@Autowired
	private DistributerService distributerService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getDocumentTrackingPage(ModelAndView mav,
			HttpServletRequest request) {
		//User currentUser = SessionUtility.getUserFromSession(request);
		ArrayList<User> distributerList = distributerService.findAllActiveDistributersList();
		ArrayList<DocumentType> docTypeList = documentTrackingService.getAllDocumentTypeList();
		ArrayList<DocumentTrackStatus> docTrackStatusList = documentTrackingService.getAllDocumentTrackStatusList();
				
		mav
		.addObject(DISTRIBUTER.DISTRIBUTER_LIST.name(), distributerList)
		.addObject(DOCUMENT.DOCUMENT_TYPE_LIST.name(), docTypeList)
		.addObject(DOCUMENT.DOCUMENT_TRACK_STATUS.name(), docTrackStatusList)
		.addObject(DOCUMENT.DOCUMENT_DETAIL.name(), new DocumentTrackingDetail())
		.setViewName("documents/documentTrack");
		
		return mav;
	}
	
	@RequestMapping(value ="/saveDocumentTrackDetail" , method = RequestMethod.POST)
	public String saveDocumentTrackDetail(@ModelAttribute(value="DOCUMENT_DETAIL")DocumentTrackingDetail documentTrackDetail, HttpServletRequest request) {
		documentTrackDetail = documentTrackingService.save(documentTrackDetail);
		System.out.println(documentTrackDetail.toString());
		return "redirect:/documents/documentTrackingController/getDocTrackDetail?userId="+documentTrackDetail.getPartner().getUserId()+"&"+PROJECT.RET_MSG.name()+"="+PROJECT.SUCCESS_MSG.name();
	}
	
	@RequestMapping(value="/getDocTrackDetail" , method = RequestMethod.GET)
	public ModelAndView getDocTrackDetail(@RequestParam("userId") Long userId) {
		ModelAndView mav = new ModelAndView();
		ArrayList<DocumentTrackingDetail> docTrackDetailList = documentTrackingService.getDocTrackDetailByPartner(userId);
		ArrayList<DocumentTrackStatus> documentTrackStatusList = documentTrackingService.getAllDocumentTrackStatusList();
		
		mav
			.addObject(DOCUMENT.DOCUMENT_PARTNER_LIST.name(), docTrackDetailList)
			.addObject(DOCUMENT.DOCUMENT_TRACK_STATUS.name(), documentTrackStatusList)
		    .setViewName("documents/documentTrackingList");
		return mav;
	}
	
	@RequestMapping(value="/getListByStatus", method = RequestMethod.GET)
	//@ResponseBody
	public ModelAndView getListByStatus(@RequestParam("statusId") Long statusId, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		ArrayList<DocumentTrackingDetail> docTrackDetailList = null;
		User currentUser = SessionUtility.getUserFromSession(request);
		if(statusId < 0) {
			docTrackDetailList = documentTrackingService.getDocTrackDetailByPartner(currentUser.getUserId());
		}else {
			docTrackDetailList = documentTrackingService.getListByStatus(statusId,currentUser.getUserId());
		}
		
		mav
			.addObject(DOCUMENT.DOCUMENT_PARTNER_LIST.name(), docTrackDetailList)
			.setViewName("documents/documentTrackingAjaxPage");
		
		return mav;
	}
}
