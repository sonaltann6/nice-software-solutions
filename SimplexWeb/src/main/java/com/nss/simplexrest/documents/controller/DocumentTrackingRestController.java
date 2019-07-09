package com.nss.simplexrest.documents.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.documents.model.DocumentTrackStatus;
import com.nss.simplexweb.documents.model.DocumentTrackingDetail;
import com.nss.simplexweb.documents.model.DocumentType;
import com.nss.simplexweb.documents.service.DocumentTrackingService;
import com.nss.simplexweb.enums.DISTRIBUTER;
import com.nss.simplexweb.enums.DOCUMENT;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.DistributerService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/documents/documentTrackingController")
@Api(value = "Documents tracking Resource REST Endpoint", description = "All Documents tracking Related Operations")
public class DocumentTrackingRestController {

	@Autowired
	private DocumentTrackingService documentTrackingService;
	
	@Autowired
	private DistributerService distributerService;
	
	@GetMapping(value="/getDocTrackingPresets")
	@ResponseBody
	public Map<String, List<?>> getDocTrackingPresets(){
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		
		ArrayList<User> distributerList = distributerService.findAllActiveDistributersList();
		ArrayList<DocumentType> docTypeList = documentTrackingService.getAllDocumentTypeList();
		ArrayList<DocumentTrackStatus> docTrackStatusList = documentTrackingService.getAllDocumentTrackStatusList();
		
		map.put(DISTRIBUTER.DISTRIBUTER_LIST.name(), distributerList);
		map.put(DOCUMENT.DOCUMENT_TYPE_LIST.name(), docTypeList);
		map.put(DOCUMENT.DOCUMENT_TRACK_STATUS.name(), docTrackStatusList);
		
		return map;
	}
	
	@PostMapping(value="/saveDocumentDetail")
	@ResponseBody
	public DocumentTrackingDetail saveDocumentDetail(@RequestBody DocumentTrackingDetail documentTrackingDetail) {
		return documentTrackingService.save(documentTrackingDetail);
	}
	
	@GetMapping(value="/getDocTrackDetail")
	@ResponseBody
	public Map<String, List<?>> getDocTrackDetail(@RequestParam("userId") Long userId){
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		ArrayList<DocumentTrackingDetail> docTrackDetailList = documentTrackingService.getDocTrackDetailByPartner(userId);
		
		map.put(DOCUMENT.DOCUMENT_PARTNER_LIST.name(), docTrackDetailList);
		return map;
	}
	
	@GetMapping(value="/getTrackDetailByStatus")
	@ResponseBody
	public Map<String, List<?>> getTrackDetailByStatus(@RequestParam("userId") Long userId){
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		map.put(DOCUMENT.DOCUMENT_DISPATCHED.name(), documentTrackingService.getListByStatusName(DOCUMENT.DOCUMENT_DISPATCHED.name(), userId));
		map.put(DOCUMENT.DOCUMENT_RECEIVED.name(), documentTrackingService.getListByStatusName(DOCUMENT.DOCUMENT_RECEIVED.name(), userId));
		return map;
	}
}
