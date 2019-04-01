package com.nss.simplexrest.po.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enquiry.template.service.EnquiryTemplateService;
import com.nss.simplexweb.enums.COMPANY;
import com.nss.simplexweb.enums.DOCUMENT_PARENT_ENTITY_TYPE;
import com.nss.simplexweb.enums.ENQUIRY;
import com.nss.simplexweb.enums.PAYMENT_TERMS;
import com.nss.simplexweb.enums.PO;
import com.nss.simplexweb.enums.PO_TRACKING;
import com.nss.simplexweb.enums.PROJECT;
import com.nss.simplexweb.enums.ROLE;
import com.nss.simplexweb.enums.SHIPMENT_TERMS;
import com.nss.simplexweb.notifications.service.NotificationService;
import com.nss.simplexweb.paymentterm.model.PaymentTerms;
import com.nss.simplexweb.paymentterm.service.PaymentTermsService;
import com.nss.simplexweb.po.model.PODetail;
import com.nss.simplexweb.po.model.POStatus;
import com.nss.simplexweb.po.model.POTrackingHistory;
import com.nss.simplexweb.po.service.PODetailService;
import com.nss.simplexweb.po.service.POStatusService;
import com.nss.simplexweb.po.service.POTrackingHistoryService;
import com.nss.simplexweb.shipmenterm.service.ShipmentTermsService;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.MainComapanyService;
import com.nss.simplexweb.user.service.UserService;
import com.nss.simplexweb.utility.document.model.Document;
import com.nss.simplexweb.utility.document.service.DocumentService;
import com.nss.simplexweb.utility.mail.MailBean;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/rest/po")
@Api(value = "PO Resource REST Endpoint", description = "All POs Related Operations")
public class PORestController {

	@Autowired
	private PODetailService poDetailService;
	
	@Autowired
	private MainComapanyService mainComapanyService;
	
	@Autowired
	private EnquiryTemplateService enquiryTemplateService;
	
	@Autowired
	private PaymentTermsService paymentTermsService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private POStatusService poStatusService;
	
	@Autowired
	private ShipmentTermsService shipmentTermsService;
	
	@Autowired
	private POTrackingHistoryService poTrackingHistoryService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private NotificationService notificationService;
	
	
	@GetMapping(value = "/getMyPORequestsList")
	public ArrayList<PODetail> getMyPORequestsList(@RequestParam ("userId") Long userId){
		User user = userService.findUserByUserId(userId);
		return poDetailService.getInProgressPOListForProcessor(user);
	}
	
	@GetMapping(value = "/placeOrder")
	public Map<String, Object> placeNewOrder(@RequestBody User user) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		ArrayList<EnquiryTemplateBean> enquiryHistoryList = enquiryTemplateService.getEnquiryHistoryForUser(user);
		
		ArrayList<PaymentTerms> paymentTermsList = null;
		if(user.getRole().getRoleAbbr().equalsIgnoreCase(ROLE.DIST.name())) {
			paymentTermsList = paymentTermsService.getPaymentTermsListByPartnerId(user.getUserId());
		}else {
			paymentTermsList = paymentTermsService.getActivePaymentTermsList();	//For employee
		}
		
		map.put(COMPANY.COMPANY.name(), mainComapanyService.getMainComapnyInfo());
		map.put(ENQUIRY.ENQUIRY_HISTORY_LIST, enquiryHistoryList);
		map.put(PAYMENT_TERMS.PYAMENT_TERMS_LIST.name(), paymentTermsList);
		map.put(SHIPMENT_TERMS.SHIPMENT_TERMS_LIST.name(), shipmentTermsService.getActiveShipmentTermsList());
		map.put(PO.PO_DETAIL.name(), new PODetail());
		return map;
	}
	@GetMapping(value = "/getPODetails")
	@ResponseBody
	public Map<String, Object> getPODetails(@RequestParam("poId") Long poId, @RequestParam("poNumber") String poNumber) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		PODetail poDetail = poDetailService.getPODetailsByPoIdAndPoNumber(poId, poNumber);
		ArrayList<Document> document = documentService.findActiveDocumentsByParentIdAndParentEntityType(poDetail.getPoId(), DOCUMENT_PARENT_ENTITY_TYPE.PO_DOCUMENT.name());
		
		map.put(COMPANY.COMPANY.name(), mainComapanyService.getMainComapnyInfo());
		map.put(PO.PO_DETAIL.name(), poDetail);
		map.put(DOCUMENT_PARENT_ENTITY_TYPE.PO_DOCUMENT.name(), document);
		
		return map;
	}
	
	@GetMapping(value = "/downloadPOAsPDF")
	public void downloadPOAsPDF(@RequestParam("poId")Long poId, @RequestParam("poNumber")String poNumber, HttpServletResponse response) {
		poDetailService.downloadPOAsPDF(poId, poNumber, response);
	}
	
	@GetMapping(value = "/getPOHistoryForUser")
	public Map<String, List<?>> getPOHistoryForUser(@RequestBody User user) throws JSONException{
		Map<String, List<?>> map = new HashMap<String, List<?>>();
		map.put(PO.PO_LIST.name(), poDetailService.getPOHistoryForUser(user));
		return map;
	}
	
	@GetMapping(value = "/getPOHistoryForUser/trackPO")
	public Map<String, Object> trackPO(@RequestParam("poId") Long poId, @RequestParam("poNumber") String poNumber) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		PODetail poDetail = poDetailService.getPODetailsByPoIdAndPoNumber(poId, poNumber);
		List<PODetail> poDetailList = new ArrayList<PODetail>();
		poDetailList.add(poDetail);
		ArrayList<POTrackingHistory> poTrackingHistoryList = poTrackingHistoryService.getPOTrackingHistoryListForPODesc(poDetail);
		
		map.put(PO_TRACKING.PO_TRACKING_HISTORY_LIST.name(), poTrackingHistoryList);
		map.put(PO.PO_STATUS.name(), poDetail);
		
		return map;
	}
	
	@GetMapping(value = "/getOpenPORequestsList")
	public ArrayList<PODetail> getOpenPORequestsList() {
		return poDetailService.getOpenPORequestsList();
	}

	@GetMapping(value = "/getMyPORequestsList/getUpdatePOStatusPage")
	public Map<String, Object> getUpdatedPOStatusPage(@RequestParam("poId") Long poId, @RequestParam("poNumber") String poNumber, @RequestParam("userId") Long userId) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = userService.findUserByUserId(userId);
		PODetail poDetail = poDetailService.getPODetailsByPoIdAndPoNumber(poId, poNumber);
		ArrayList<POStatus> poNextApplicableStatusType = poStatusService.getPONextApplicableStatusType(poDetail.getPoStatus());
		ArrayList<POTrackingHistory> poTrackingHistoryList = poTrackingHistoryService.getPOTrackingHistoryListForPODesc(poDetail);
		ArrayList<PODetail> inProgressPOListForProcessor = poDetailService.getInProgressPOListForProcessor(user);
		
		map.put(PO.PO_DETAIL.name(), poDetail);
		map.put(PO_TRACKING.PO_TRACKING_HISTORY.name(),  new POTrackingHistory());
		map.put(PO.IN_PROGRESS_PO_LIST.name(), inProgressPOListForProcessor);
		map.put(PO_TRACKING.PO_TRACKING_HISTORY_LIST.name(), poTrackingHistoryList);
		map.put(PO_TRACKING.PO_NEXT_APPLICABLE_STATUS_TYPE.name(), poNextApplicableStatusType);
		
		return map;
	}
	
	@GetMapping(value = "/getPODetailsBeforeProcessing")
	public Map<String, Object> getPODetailsBeforeProcessing(@RequestParam("poId") Long poId, @RequestParam("poNumber") String poNumber) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();
		PODetail poDetail = poDetailService.getPODetailsByPoIdAndPoNumber(poId, poNumber);
		ArrayList<Document> document = documentService.findActiveDocumentsByParentIdAndParentEntityType(poDetail.getPoId(), DOCUMENT_PARENT_ENTITY_TYPE.PO_DOCUMENT.name());
		
		map.put(COMPANY.COMPANY.name(), mainComapanyService.getMainComapnyInfo());
		map.put(PO.PO_DETAIL.name(), poDetail);
		map.put(DOCUMENT_PARENT_ENTITY_TYPE.PO_DOCUMENT.name(), document);
		return map;
	}
	
	@PostMapping(value = "/processNewPO")
	public PODetail processNewPO(@RequestParam("poId") Long poId, @RequestParam("poNumber") String poNumber, @RequestParam("userId") Long userId) {
		User user  = userService.findUserByUserId(userId);
		PODetail poDetail = poDetailService.getPODetailsByPoIdAndPoNumber(poId, poNumber);
		poDetail.setProcessor(user);
		poDetail.setStatusUpdatedBy(user);
		poDetail.setPoStatus(poStatusService.getPOStatusByPoStatusAbbr(PO_TRACKING.PO_UNDER_PROCESS.name()));
		poDetail = poDetailService.updatePOStatus(poDetail);
		
		POTrackingHistory poTrackingHistory = new POTrackingHistory();
		poTrackingHistory.setPoId(poDetail);
		poTrackingHistory.setPoStatus(poDetail.getPoStatus());
		poTrackingHistory.setPoTrackingComment(poDetail.getPoStatus().getPoStatusName());
		poTrackingHistory.setUpdatedBy(user);
		poTrackingHistoryService.addPOTrackingEntry(poTrackingHistory);
		
		return poDetail;
	}
	
	@PostMapping(value = "/saveNewOrderWithOutFile")
	@ResponseBody
	public PODetail saveNewOrder(@RequestBody PODetail poDetail, HttpServletRequest request) {
		@SuppressWarnings("unused")
		User currentUser = userService.findUserByUserId(poDetail.getRequester().getUserId());
		poDetail = poDetailService.saveNewPurchaseOrder(poDetail);
		notificationService.saveNewPONotification(poDetail, 3);
		return poDetail;
	}
	
	@PostMapping(value = "/saveNewOrder")
	@ResponseBody
	public PODetail saveNewOrder(@RequestParam(value = "ionicfile", required = false) MultipartFile file, 
			HttpServletRequest request,
			@RequestParam("poDetail") String base64Json) throws JsonParseException, JsonMappingException, IOException {
		
		String json = new String(Base64.getDecoder().decode(base64Json));		
		ObjectMapper mapper = new ObjectMapper();
		PODetail poDetail = mapper.readValue(json, PODetail.class);
		
		System.out.println(poDetail.toString());
		User currentUser = userService.findUserByUserId(poDetail.getRequester().getUserId());
		poDetail = poDetailService.saveNewPurchaseOrder(poDetail);
		poDetailService.savePODocument(file, poDetail, currentUser);
		notificationService.saveNewPONotification(poDetail, 3);
 		return new PODetail();
	}
	
	@PutMapping(value = "/updatePOStatus")
	public String updatePOStatus(@RequestBody POTrackingHistory poTrackingHistory, @RequestParam("userId") Long userId) throws JSONException {
		JSONObject obj = new JSONObject();
		PODetail poDetail = poDetailService.getPODetailsByPoId(poTrackingHistory.getPoId().getPoId());
		User user = userService.findUserByUserId(userId);
		poDetail.setStatusUpdatedBy(user);
		poDetail.setPoStatus(poTrackingHistory.getPoStatus());
		poDetailService.updatePOStatus(poDetail);
		
		poTrackingHistory.setUpdatedBy(user);
		poTrackingHistoryService.addPOTrackingEntry(poTrackingHistory);
		
		obj.put(PROJECT.SUCCESS_MSG.name(),poTrackingHistory);
		return obj.toString();
	}
	
	@PostMapping(value = "/emailPurchaseOrder")
	public Map<String, Object> emailPurchaseOrder(@RequestParam("poId") Long poId, @RequestParam("poNumber") String poNumber, @RequestBody MailBean mailBean){
		Map<String, Object> map = new HashMap<String, Object>();
		
		return map;
	}
}
