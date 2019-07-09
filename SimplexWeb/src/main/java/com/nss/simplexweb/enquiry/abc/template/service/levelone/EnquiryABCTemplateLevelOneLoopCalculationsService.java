package com.nss.simplexweb.enquiry.abc.template.service.levelone;

import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("enquiryABCTemplateLevelOneLoopCalculationsService")
public class EnquiryABCTemplateLevelOneLoopCalculationsService {

	// Perform Level One Calculations For Loop
	public EnquiryTemplateBean performLevelOneCalculationsForLoop(EnquiryTemplateBean enquiryTemplateBean) {
		// Total loops required
		if (enquiryTemplateBean.getProductModelType().getModelTypeAbbr().equalsIgnoreCase(ENQUIRY.MODEL_TYPE_UPANEL)
				|| enquiryTemplateBean.getProductModelType().getModelTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.MODEL_TYPE_4PANEL)
				|| enquiryTemplateBean.getProductModelType().getModelTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.MODEL_TYPE_Q_BAFFLE)
				|| enquiryTemplateBean.getProductModelType().getModelTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.MODEL_TYPE_TUBULAR_CIRCULAR)) {
			enquiryTemplateBean.setLoopNumber(4.0);

		} else if (enquiryTemplateBean.getProductModelType().getModelTypeAbbr()
				.equalsIgnoreCase(ENQUIRY.MODEL_TYPE_SINGLE_LOOP)) {
			enquiryTemplateBean.setLoopNumber(1.0);

		} else if (enquiryTemplateBean.getProductModelType().getModelTypeAbbr()
				.equalsIgnoreCase(ENQUIRY.MODEL_TYPE_TWO_LOOP)) {
			enquiryTemplateBean.setLoopNumber(2.0);
		}
		
		return enquiryTemplateBean;
	}
}
