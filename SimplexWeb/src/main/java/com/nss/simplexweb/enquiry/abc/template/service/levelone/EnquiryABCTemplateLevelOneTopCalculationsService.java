package com.nss.simplexweb.enquiry.abc.template.service.levelone;

import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("enquiryABCTemplateLevelOneTopCalculationsService")
public class EnquiryABCTemplateLevelOneTopCalculationsService {
	
	//DEFAULT coated value
	private static final Double DEFAULT_COATED_VALUE = 100.0;
	
	public EnquiryTemplateBean performLevelOneCalculationsForTop(EnquiryTemplateBean enquiryTemplateBean) {
		
		//1.1. Top Filling Status
			if(enquiryTemplateBean.isTopFilling()) {
				if(enquiryTemplateBean.getTopFillingType().getTopFillingTypeAbbr().equalsIgnoreCase(ENQUIRY.TOP_FILLING_TYPE_COATED)) {
					enquiryTemplateBean.setTopSkirtGSM(DEFAULT_COATED_VALUE);
					enquiryTemplateBean.setTopSpoutGSM(DEFAULT_COATED_VALUE);
					enquiryTemplateBean.setTopFlapGSM(DEFAULT_COATED_VALUE);
				}
			}
		//1.2. Top Tie Calculations
		enquiryTemplateBean = performTopTieCalculations(enquiryTemplateBean);
			
		return enquiryTemplateBean;	
	}
	
	// Top Tie Calculations
	private EnquiryTemplateBean performTopTieCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		//a. Top standard tie
		if(!enquiryTemplateBean.isTopStandardTie()) {
			enquiryTemplateBean.setTopStandardTie(true);
		}
		//b. Top rope tie
		if(!enquiryTemplateBean.isTopRopeTie()){
			enquiryTemplateBean.setTopRopeTie(true);
		}
		//c. Top velcro tie
		if(!enquiryTemplateBean.isTopVelcroTie()){
			enquiryTemplateBean.setTopVelcroTie(true);
		}
		//d. Top b-lcok
		if(!enquiryTemplateBean.isTopBlock()){
			enquiryTemplateBean.setTopBlock(true);
		}
		return enquiryTemplateBean;
	}
}
