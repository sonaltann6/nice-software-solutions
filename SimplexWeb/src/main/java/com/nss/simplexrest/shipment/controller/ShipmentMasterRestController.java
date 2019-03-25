package com.nss.simplexrest.shipment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.shipmenterm.model.ShipmentTerms;
import com.nss.simplexweb.shipmenterm.service.ShipmentTermsService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/shipment")
@Api(value = "Shipment Resource REST Endpoint", description = "All Shipment Related Operations")
public class ShipmentMasterRestController {
	
	@Autowired
	private ShipmentTermsService shipmentTermsService;
	
	@GetMapping(value = "/getShipmentTermsList")
	public Object getShipmentTermsList() {
		return shipmentTermsService.getActiveShipmentTermsList();
	}
	
	@GetMapping(value = "/getShipmentTermDetailsById")
	public ShipmentTerms getShipmentTermDetailsById(@RequestParam ("shipmentTermId") Long shipmentTermId) {
		return shipmentTermsService.getShipmentTermDetailsById(shipmentTermId);
	}
	
	@DeleteMapping(value = "/deleteShipmentTermDetailsById")
	public ShipmentTerms deleteShipmentTermDetailsById(@RequestParam ("shipmentTermId") Long shipmentTermId) {
		return shipmentTermsService.deleteShipmentTermDetailsById(shipmentTermId);
	}
	
	@PostMapping(value = "/saveNewShipmentTerm")
	public ShipmentTerms saveNewShipmentTerm(@RequestBody ShipmentTerms shipmentTerms) {
		//shipmentTerms.setCreatedBy(user); 
		shipmentTermsService.saveNewShipmentTerm(shipmentTerms);
		return shipmentTerms;
	}
	
	
}
