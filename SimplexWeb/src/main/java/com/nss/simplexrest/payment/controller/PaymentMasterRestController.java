package com.nss.simplexrest.payment.controller;


import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.enums.PAYMENT_TERMS;
import com.nss.simplexweb.paymentterm.model.PaymentTerms;
import com.nss.simplexweb.paymentterm.service.PaymentTermsService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/payment")
@Api(value = "Payment Resource REST Endpoint", description = "All Payment Related Operations")
public class PaymentMasterRestController {

	@Autowired
	private PaymentTermsService paymentTermsService;
	
	@GetMapping(value = "/getPaymentTermsList")
	public ArrayList<PaymentTerms> getPaymentTermsList(){
		return paymentTermsService.getActivePaymentTermsList();
	}
	
	@GetMapping(value = "/getPaymentTermDetailsById")
	public PaymentTerms getPaymentTermDetailsById(@RequestParam ("paymentTermId") Long paymentTermId) {
		return paymentTermsService.getPaymentTermDetailsById(paymentTermId);
	}
	
	@DeleteMapping(value = "/deletePaymentTermDetailsById")
	public PaymentTerms deletePaymentTermDetailsById(@RequestParam ("paymentTermId") Long paymentTermId) {
		return paymentTermsService.deletePaymentTermDetailsById(paymentTermId);
	}
	
	@GetMapping(value = "/getPaymentTermsListByPartnerId")
	public HashMap<String, ArrayList<?>> getPaymentTermsListByPartnerId(@RequestParam ("partnerId") Long partnerId) {
		HashMap<String, ArrayList<?>> map = new HashMap<>();
		map.put(PAYMENT_TERMS.PAYMENT_TERMS_LIST_FOR_PARTNER.name(), paymentTermsService.getPaymentTermsListByPartnerId(partnerId));
		map.put(PAYMENT_TERMS.PAYMENT_TERMS_LIST.name(), paymentTermsService.getActivePaymentTermsList());
		return map;
	}
	
	@PostMapping(value = "/saveNewPaymentTerm")
	public PaymentTerms saveNewPaymentTerm(@RequestBody PaymentTerms paymentTerms) {
		//paymentTerms.setCreatedBy(user);
		paymentTermsService.saveNewPaymentTerm(paymentTerms);
		return paymentTerms;
	}
	
	/*@PutMapping(value = "/updatePaymentTermsForDistributer")
	public String updatePaymentTermsForDistributer(@RequestBody User user, @RequestParam ("paymentTermsList") ArrayList<PaymentTerms> paymentTermsList) {
		return paymentTermsService.savePaymentTermsForDistributer(user, paymentTermsList);
	}*/
}
