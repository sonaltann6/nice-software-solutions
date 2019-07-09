package com.nss.simplexweb.enquiry.abc.template.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.abc.template.service.levelone.EnquiryABCTemplateLevelOneBottomCalculationsService;
import com.nss.simplexweb.enquiry.abc.template.service.levelone.EnquiryABCTemplateLevelOneCalculationsService;
import com.nss.simplexweb.enquiry.abc.template.service.levelone.EnquiryABCTemplateLevelOneFabricCalculationsService;
import com.nss.simplexweb.enquiry.abc.template.service.levelone.EnquiryABCTemplateLevelOneLoopCalculationsService;
import com.nss.simplexweb.enquiry.abc.template.service.levelone.EnquiryABCTemplateLevelOneOtherCalculationsService;
import com.nss.simplexweb.enquiry.abc.template.service.levelone.EnquiryABCTemplateLevelOneProductCalculationsService;
import com.nss.simplexweb.enquiry.abc.template.service.levelone.EnquiryABCTemplateLevelOneTopCalculationsService;
import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enquiry.template.repository.EnquiryTemplateRepository;
import com.nss.simplexweb.enquiry.template.service.EnquiryGlobalTemplateLevelTwo4PanelCalculationsService;
import com.nss.simplexweb.enquiry.template.service.EnquiryGlobalTemplateLevelTwoBafflePanelCalculationsService;
import com.nss.simplexweb.enquiry.template.service.EnquiryGlobalTemplateLevelTwoSingleLoopCalculationsService;
import com.nss.simplexweb.enquiry.template.service.EnquiryGlobalTemplateLevelTwoTubularCalculationsService;
import com.nss.simplexweb.enquiry.template.service.EnquiryGlobalTemplateLevelTwoTwoLoopCalculationsService;
import com.nss.simplexweb.enquiry.template.service.EnquiryGlobalTemplateLevelTwoUPanelCalculationsService;

@Service("enquiryABCTemplateService")
public class EnquiryABCTemplateService {

	@Autowired
	private EnquiryTemplateRepository enquiryTemplateRepository;
	
	@Autowired
	private EnquiryABCTemplateLevelOneCalculationsService enquiryABCTemplateLevelOneCalculationsService;

	@Autowired
	private EnquiryABCTemplateLevelOneProductCalculationsService enquiryABCTemplateLevelOneProductCalculationsService;

	@Autowired
	private EnquiryABCTemplateLevelOneFabricCalculationsService enquiryABCTemplateLevelOneFabricCalculationsService;

	@Autowired
	private EnquiryABCTemplateLevelOneTopCalculationsService enquiryABCTemplateLevelOneTopCalculationsService;

	@Autowired
	private EnquiryABCTemplateLevelOneBottomCalculationsService enquiryABCTemplateLevelOneBottomCalculationsService;

	@Autowired
	private EnquiryABCTemplateLevelOneLoopCalculationsService enquiryABCTemplateLevelOneLoopCalculationsService;

	@Autowired
	private EnquiryABCTemplateLevelOneOtherCalculationsService enquiryABCTemplateLevelOneOtherCalculationsService;

	@Autowired
	private EnquiryGlobalTemplateLevelTwo4PanelCalculationsService enquiryGlobalTemplateLevelTwo4PanelCalculationsService;

	@Autowired
	private EnquiryGlobalTemplateLevelTwoBafflePanelCalculationsService enquiryGlobalTemplateLevelTwoBaffleCalculationsService;

	@Autowired
	private EnquiryGlobalTemplateLevelTwoSingleLoopCalculationsService enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService;

	@Autowired
	private EnquiryGlobalTemplateLevelTwoTubularCalculationsService enquiryGlobalTemplateLevelTwoTubularCalculationsService;

	@Autowired
	private EnquiryGlobalTemplateLevelTwoTwoLoopCalculationsService enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService;

	@Autowired
	private EnquiryGlobalTemplateLevelTwoUPanelCalculationsService enquiryGlobalTemplateLevelTwoUPanelCalculationsService;

	public EnquiryABCTemplateService(
			EnquiryTemplateRepository enquiryTemplateRepository,
			EnquiryABCTemplateLevelOneCalculationsService enquiryABCTemplateLevelOneCalculationsService,
			EnquiryABCTemplateLevelOneProductCalculationsService enquiryABCTemplateLevelOneProductCalculationsService,
			EnquiryABCTemplateLevelOneFabricCalculationsService enquiryABCTemplateLevelOneFabricCalculationsService,
			EnquiryABCTemplateLevelOneTopCalculationsService enquiryABCTemplateLevelOneTopCalculationsService,
			EnquiryABCTemplateLevelOneBottomCalculationsService enquiryABCTemplateLevelOneBottomCalculationsService,
			EnquiryABCTemplateLevelOneLoopCalculationsService enquiryABCTemplateLevelOneLoopCalculationsService,
			EnquiryABCTemplateLevelOneOtherCalculationsService enquiryABCTemplateLevelOneOtherCalculationsService,
			EnquiryGlobalTemplateLevelTwo4PanelCalculationsService enquiryGlobalTemplateLevelTwo4PanelCalculationsService,
			EnquiryGlobalTemplateLevelTwoBafflePanelCalculationsService enquiryGlobalTemplateLevelTwoBaffleCalculationsService,
			EnquiryGlobalTemplateLevelTwoSingleLoopCalculationsService enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService,
			EnquiryGlobalTemplateLevelTwoTubularCalculationsService enquiryGlobalTemplateLevelTwoTubularCalculationsService,
			EnquiryGlobalTemplateLevelTwoTwoLoopCalculationsService enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService,
			EnquiryGlobalTemplateLevelTwoUPanelCalculationsService enquiryGlobalTemplateLevelTwoUPanelCalculationsService) {

		this.enquiryTemplateRepository = enquiryTemplateRepository;
		this.enquiryABCTemplateLevelOneCalculationsService = enquiryABCTemplateLevelOneCalculationsService;
		this.enquiryABCTemplateLevelOneProductCalculationsService = enquiryABCTemplateLevelOneProductCalculationsService;
		this.enquiryABCTemplateLevelOneFabricCalculationsService = enquiryABCTemplateLevelOneFabricCalculationsService;
		this.enquiryABCTemplateLevelOneTopCalculationsService = enquiryABCTemplateLevelOneTopCalculationsService;
		this.enquiryABCTemplateLevelOneBottomCalculationsService = enquiryABCTemplateLevelOneBottomCalculationsService;
		this.enquiryABCTemplateLevelOneLoopCalculationsService = enquiryABCTemplateLevelOneLoopCalculationsService;
		this.enquiryABCTemplateLevelOneOtherCalculationsService = enquiryABCTemplateLevelOneOtherCalculationsService;
		this.enquiryGlobalTemplateLevelTwo4PanelCalculationsService = enquiryGlobalTemplateLevelTwo4PanelCalculationsService;
		this.enquiryGlobalTemplateLevelTwoBaffleCalculationsService = enquiryGlobalTemplateLevelTwoBaffleCalculationsService;
		this.enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService = enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService;
		this.enquiryGlobalTemplateLevelTwoTubularCalculationsService = enquiryGlobalTemplateLevelTwoTubularCalculationsService;
		this.enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService = enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService;
		this.enquiryGlobalTemplateLevelTwoUPanelCalculationsService = enquiryGlobalTemplateLevelTwoUPanelCalculationsService;
	}

	// 1. Perform All level one calculations
	private EnquiryTemplateBean performLevelOneCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		// 1. Level one calculations
		enquiryTemplateBean = enquiryABCTemplateLevelOneCalculationsService
				.performLevelOneCalculations(enquiryTemplateBean);

		// 2. Product : level one calc
		enquiryTemplateBean = enquiryABCTemplateLevelOneProductCalculationsService
				.performLevelOneCalculationsForProduct(enquiryTemplateBean);

		// 3. Fabric : level one calc
		enquiryTemplateBean = enquiryABCTemplateLevelOneFabricCalculationsService
				.performLevelOneCalculationsForFabric(enquiryTemplateBean);

		// 4. Top : level one calc
		enquiryTemplateBean = enquiryABCTemplateLevelOneTopCalculationsService
				.performLevelOneCalculationsForTop(enquiryTemplateBean);

		// 5. Bottom : level one calc
		enquiryTemplateBean = enquiryABCTemplateLevelOneBottomCalculationsService
				.performLevelOneCalculationsForBottom(enquiryTemplateBean);

		// 6. Loop : level one calc
		enquiryTemplateBean = enquiryABCTemplateLevelOneLoopCalculationsService
				.performLevelOneCalculationsForLoop(enquiryTemplateBean);

		// 7. Other : level one calc
		enquiryTemplateBean = enquiryABCTemplateLevelOneOtherCalculationsService
				.performLevelOneCalculationsForOther(enquiryTemplateBean);

		// Shipment calculations :)

		return enquiryTemplateBean;
	}

	// 2. Perform All level two calculations
	// 2.1 Perform All level two 4 panel calculations
	private EnquiryTemplateBean performLevelTwo4PanelCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		// All level two calculations
		enquiryGlobalTemplateLevelTwo4PanelCalculationsService.calculateBagWeight(enquiryTemplateBean);
		enquiryGlobalTemplateLevelTwo4PanelCalculationsService.calculateBagCost(enquiryTemplateBean);
		return enquiryTemplateBean;
	}

	// 2.2 Perform All level two baffle panel calculations
	private EnquiryTemplateBean performLevelTwoBaffleCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		// All level two calculations
		enquiryGlobalTemplateLevelTwoBaffleCalculationsService.calculateBagWeight(enquiryTemplateBean);
		enquiryGlobalTemplateLevelTwoBaffleCalculationsService.calculateBagCost(enquiryTemplateBean);
		return enquiryTemplateBean;
	}

	// 2.6 Perform All level two single loop calculations
	private EnquiryTemplateBean performLevelTwoSingleLoopCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		// All level two calculations
		enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService.calculateBagWeight(enquiryTemplateBean);
		enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService.calculateBagCost(enquiryTemplateBean);
		return enquiryTemplateBean;
	}

	// 2.7 Perform All level two tubular calculations
	private EnquiryTemplateBean performLevelTwoTubularCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		// All level two calculations
		enquiryGlobalTemplateLevelTwoTubularCalculationsService.calculateBagWeight(enquiryTemplateBean);
		enquiryGlobalTemplateLevelTwoTubularCalculationsService.calculateBagCost(enquiryTemplateBean);
		return enquiryTemplateBean;
	}

	// 2.8 Perform All level two two loop calculations
	private EnquiryTemplateBean performLevelTwoTwoLoopCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		// All level two calculations
		enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService.calculateBagWeight(enquiryTemplateBean);
		enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService.calculateBagCost(enquiryTemplateBean);
		return enquiryTemplateBean;
	}

	// 2.9 Perform All level two upanel calculations
	private EnquiryTemplateBean performLevelTwoUpanelCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		// All level two calculations
		enquiryGlobalTemplateLevelTwoUPanelCalculationsService.calculateBagWeight(enquiryTemplateBean);
		enquiryGlobalTemplateLevelTwoUPanelCalculationsService.calculateBagCost(enquiryTemplateBean);
		return enquiryTemplateBean;
	}

	// 3 Perform all calculations
	// 3.1 Perform all 4 panel calculations
	public EnquiryTemplateBean performAll4PanelCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
		enquiryTemplateBean = performLevelTwo4PanelCalculations(enquiryTemplateBean);
		return enquiryTemplateBean;
	}

	//3.2 Perform all baffle panel calculations
	public EnquiryTemplateBean performAllBaffleCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
		enquiryTemplateBean = performLevelTwoBaffleCalculations(enquiryTemplateBean);
		return enquiryTemplateBean;
	}
	
	//3.6 Perform all single loop calculations
	public EnquiryTemplateBean performAllSingleLoopCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
		enquiryTemplateBean = performLevelTwoSingleLoopCalculations(enquiryTemplateBean);
		return enquiryTemplateBean;
	}
	
	//3.7 Perform all tubular calculations
	public EnquiryTemplateBean performAllTubularCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
		enquiryTemplateBean = performLevelTwoTubularCalculations(enquiryTemplateBean);
		return enquiryTemplateBean;
	}
	
	//3.8 Perform all two loop calculations
	public EnquiryTemplateBean performAllTwoLoopCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
		enquiryTemplateBean = performLevelTwoTwoLoopCalculations(enquiryTemplateBean);
		return enquiryTemplateBean;
	}
	
	//3.9 Perform all upanel calculations
	public EnquiryTemplateBean performAllUpanelCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
		enquiryTemplateBean = performLevelTwoUpanelCalculations(enquiryTemplateBean);
		return enquiryTemplateBean;
	}
	
	public EnquiryTemplateBean saveNewEnquiry(EnquiryTemplateBean enquiryBean) {
		return enquiryTemplateRepository.save(enquiryBean);
	}
	
	public EnquiryTemplateBean getEnquiryDetailsByEnquiryId(Long enquiryId) {
		return enquiryTemplateRepository.findByEnquiryId(enquiryId);
	}

	public EnquiryTemplateBean getEnquiryDetailsByEnquiryNumber(String enquiryNumber) {
		return enquiryTemplateRepository.findByEnquiryNumber(enquiryNumber);
	}
	
	public EnquiryTemplateBean getEnquiryDetailsByEnquiryIdAndEnquiryNumber(Long enquiryId, String enquiryNumber) {
		return enquiryTemplateRepository.findByEnquiryIdAndEnquiryNumber(enquiryId, enquiryNumber);
	}
}
