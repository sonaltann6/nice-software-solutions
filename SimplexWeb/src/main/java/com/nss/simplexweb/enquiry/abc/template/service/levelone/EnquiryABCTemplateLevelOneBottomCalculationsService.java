package com.nss.simplexweb.enquiry.abc.template.service.levelone;

import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("enquiryABCTemplateLevelOneBottomCalculationsService")
public class EnquiryABCTemplateLevelOneBottomCalculationsService {

	// Perform Level One Calculations For Bottom
	public EnquiryTemplateBean performLevelOneCalculationsForBottom(EnquiryTemplateBean enquiryTemplateBean) {
		//1.1. Bottom discharge status
		if(enquiryTemplateBean.isBottomDischarge()) {
			if(enquiryTemplateBean.getBottomDischargeType().getBottomDischargeTypeAbbr().equalsIgnoreCase(ENQUIRY.BOTTOM_DISCHARGE_TYPE_COATED)) {
				if(enquiryTemplateBean.getBottomSpoutGSM() == null){
					if(enquiryTemplateBean.getSwl() >= 1500 && enquiryTemplateBean.getSwl()<=2500){
						enquiryTemplateBean.setBottomSpoutGSM(100.0);
					}else if(enquiryTemplateBean.getSwl() >= 2500 && enquiryTemplateBean.getSwl()<=4000) {
						enquiryTemplateBean.setBottomSpoutGSM(120.0);
					}else if(enquiryTemplateBean.getSwl() >= 4000) {
						enquiryTemplateBean.setBottomSpoutGSM(130.0);
					}
				}
			}
		}
		//1.2. Bottom Tie Calculations
		enquiryTemplateBean = performBottomTieCalculations(enquiryTemplateBean);
		
		return enquiryTemplateBean;
	}
	// Bottom Tie Calculations
	private EnquiryTemplateBean performBottomTieCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		//a. Bottom standard tie
		if(!enquiryTemplateBean.isBottomStandardTie()){
			enquiryTemplateBean.setBottomBlock(true);
		}
		//b. Bottom rope tie
		if(!enquiryTemplateBean.isBottomRopeTie()){
			enquiryTemplateBean.setBottomRopeTie(true);
		}
		//c. Bottom velcro tie
		if(!enquiryTemplateBean.isBottomVelcroTie()){
			enquiryTemplateBean.setBottomVelcroTie(true);
		}
		//d. Bottom b-lcok
		if(!enquiryTemplateBean.isBottomBlock()){
			enquiryTemplateBean.setBottomBlock(true);
		}
		return enquiryTemplateBean;
	}
}
