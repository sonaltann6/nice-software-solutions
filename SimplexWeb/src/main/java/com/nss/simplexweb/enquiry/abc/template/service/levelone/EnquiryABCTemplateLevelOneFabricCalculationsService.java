package com.nss.simplexweb.enquiry.abc.template.service.levelone;

import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("enquiryABCTemplateLevelOneFabricCalculationsService")
public class EnquiryABCTemplateLevelOneFabricCalculationsService {

	//Default fabric coated value
	private static final Double DEFAULT_FABRIC_COATED_VALUE = 30.0;
	
	//Default fabric un-coated value
	private static final Double DEFAULT_FABRIC_UNCOATED_VALUE = 150.0;
	
	//Default fabric reinforcement value
	private static final Double DEFAULT_FABRIC_REINFORCEMENT_VALUE = 15.0;
  
  
	//Perform Level One Calculations For Fabric
	public EnquiryTemplateBean performLevelOneCalculationsForFabric(EnquiryTemplateBean enquiryTemplateBean) {
		//1. Fabric GSM
		if(enquiryTemplateBean.getFabricGSMValue() == null){
			//2. Fabric coated
			if(enquiryTemplateBean.getFabricGSMType().getFabricGSMTypeAbbr().equals(ENQUIRY.FABRIC_GSM_COATED)){
				enquiryTemplateBean.setFabricGSMValue(DEFAULT_FABRIC_COATED_VALUE);
				if(enquiryTemplateBean.getProductUnitTypeWeight().getUnitTypeAbbr().equalsIgnoreCase(ENQUIRY.UNIT_OUNCES)){
					enquiryTemplateBean.setFabricGSMValue(enquiryTemplateBean.getFabricGSMValue() * 33.906);
				}
			}else if(enquiryTemplateBean.getFabricGSMType().getFabricGSMTypeAbbr().equals(ENQUIRY.FABRIC_GSM_UNCOATED)){
				enquiryTemplateBean.setFabricGSMValue(DEFAULT_FABRIC_UNCOATED_VALUE); 
				if(enquiryTemplateBean.getProductUnitTypeWeight().getUnitTypeAbbr().equalsIgnoreCase(ENQUIRY.UNIT_OUNCES)){
					enquiryTemplateBean.setFabricGSMValue(enquiryTemplateBean.getFabricGSMValue() * 33.906);
				}
			}
		}
		
		//3. Fabric reinforcement
		if(enquiryTemplateBean.getFabricType().getFabricTypeAbbr().equals(ENQUIRY.FABRIC_TYPE_TUBULAR_FABRIC)){
			enquiryTemplateBean.setFabricGSMValue(enquiryTemplateBean.getFabricGSMValue() + DEFAULT_FABRIC_REINFORCEMENT_VALUE);
		}
		
		//4. bag seam color
		if(enquiryTemplateBean.getFabricBagSeamColor() == null) {
			System.out.println("white");
		}
		return enquiryTemplateBean;
	}
}