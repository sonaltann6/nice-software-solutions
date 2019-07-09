package com.nss.simplexweb.enquiry.abc.template.service.levelone;

import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("enquiryABCTemplateLevelOneProductCalculationsService")
public class EnquiryABCTemplateLevelOneProductCalculationsService {

	// Default baffle GSM value
	private static final Double DEFAULT_BAFFLE_GSM_VALUE = 150.0;

	// Perform Level One Calculations For product
	public EnquiryTemplateBean performLevelOneCalculationsForProduct(EnquiryTemplateBean enquiryTemplateBean) {

		// 1. Baffle
		if (enquiryTemplateBean.isBaffle()) {
			if (enquiryTemplateBean.getBaffleValue() == null) {
				enquiryTemplateBean.setBaffleValue(DEFAULT_BAFFLE_GSM_VALUE);
			} else {
				if (enquiryTemplateBean.getProductUnitTypeWeight().getUnitTypeAbbr()
						.equalsIgnoreCase(ENQUIRY.UNIT_OUNCES)) {
					enquiryTemplateBean.setBaffleValue(enquiryTemplateBean.getBaffleValue() * 33.906);
				}
			}
		}
		// 2. SWL
		if (enquiryTemplateBean.getSwl() == null) {
			enquiryTemplateBean.setSwl(0.0);
		}

		// 3. uv and un
		if (!enquiryTemplateBean.isUn()) {
			enquiryTemplateBean.setUn(true);
		}
		if (!enquiryTemplateBean.isUv()) {
			enquiryTemplateBean.setUv(true);
		}

		return enquiryTemplateBean;
	}
}
