package com.nss.simplexweb.enquiry.template.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.EnquiryTemplateBean;
import com.nss.simplexweb.enquiry.template.repository.EnquiryTemplateRepository;
import com.nss.simplexweb.enquiry.template.service.calculations.levelone.EnquiryGlobalTemplateLevelOneBottomCalculationsService;
import com.nss.simplexweb.enquiry.template.service.calculations.levelone.EnquiryGlobalTemplateLevelOneCalculationsService;
import com.nss.simplexweb.enquiry.template.service.calculations.levelone.EnquiryGlobalTemplateLevelOneFabricCalculationsService;
import com.nss.simplexweb.enquiry.template.service.calculations.levelone.EnquiryGlobalTemplateLevelOneLoopCalculationsService;
import com.nss.simplexweb.enquiry.template.service.calculations.levelone.EnquiryGlobalTemplateLevelOneOtherCalculationsService;
import com.nss.simplexweb.enquiry.template.service.calculations.levelone.EnquiryGlobalTemplateLevelOneProductCalculationsService;
import com.nss.simplexweb.enquiry.template.service.calculations.levelone.EnquiryGlobalTemplateLevelOneTopCalculationsService;
import com.nss.simplexweb.po.repository.POStatusRepository;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.utility.mail.EmailController;
import com.nss.simplexweb.utility.mail.MailBean;
import com.nss.simplexweb.utility.pdf.PdfGeneratorUtilController;

@Service("enquiryTemplateService")
public class EnquiryTemplateService {
	
	@Autowired
	private EmailController emailController;
	
	@Autowired
	private PdfGeneratorUtilController pdfGeneratorUtilController;
	
	@Autowired
	private EnquiryTemplateRepository enquiryTemplateRepository;
	
	@Autowired
	private EnquiryGlobalTemplateLevelOneCalculationsService enquiryGlobalTemplateLevelOneCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelOneProductCalculationsService enquiryGlobalTemplateLevelOneProductCalculationsService;

	@Autowired
	private EnquiryGlobalTemplateLevelOneFabricCalculationsService enquiryGlobalTemplateLevelOneFabricCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelOneTopCalculationsService enquiryGlobalTemplateLevelOneTopcCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelOneBottomCalculationsService enquiryGlobalTemplateLevelOneBottomcCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelOneLoopCalculationsService enquiryGlobalTemplateLevelOneLoopCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelOneOtherCalculationsService enquiryGlobalTemplateLevelOneOtherCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelTwo4PanelCalculationsService enquiryGlobalTemplateLevelTwo4PanelCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelTwoBafflePanelCalculationsService enquiryGlobalTemplateLevelTwoBaffleCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelTwoContainerLinerCalculationsService enquiryGlobalTemplateLevelTwoContainerLinerCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelTwoHoodBagCalculationsService enquiryGlobalTemplateLevelTwoHoodBagCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelTwoPlatenCalculationsService enquiryGlobalTemplateLevelTwoPlatenCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelTwoSingleLoopCalculationsService enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelTwoTubularCalculationsService enquiryGlobalTemplateLevelTwoTubularCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelTwoTwoLoopCalculationsService enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService;
	
	@Autowired
	private EnquiryGlobalTemplateLevelTwoUPanelCalculationsService enquiryGlobalTemplateLevelTwoUPanelCalculationsService;
	
	@Autowired
	public EnquiryTemplateService(
			EnquiryTemplateRepository enquiryTemplateRepository,
			POStatusRepository pOStatusRepository,
			EnquiryGlobalTemplateLevelOneProductCalculationsService enquiryGlobalTemplateLevelOneProductCalculationsService,
			EnquiryGlobalTemplateLevelOneFabricCalculationsService enquiryGlobalTemplateLevelOneFabricCalculationsService,
			EnquiryGlobalTemplateLevelOneTopCalculationsService enquiryGlobalTemplateLevelOneTopcCalculationsService,
			EnquiryGlobalTemplateLevelOneBottomCalculationsService enquiryGlobalTemplateLevelOneBottomcCalculationsService,
			EnquiryGlobalTemplateLevelOneLoopCalculationsService enquiryGlobalTemplateLevelOneLoopCalculationsService,
			EnquiryGlobalTemplateLevelOneOtherCalculationsService enquiryGlobalTemplateLevelOneOtherCalculationsService,
			EnquiryGlobalTemplateLevelTwo4PanelCalculationsService enquiryGlobalTemplateLevelTwo4PanelCalculationsService, 
			EnquiryGlobalTemplateLevelTwoBafflePanelCalculationsService enquiryGlobalTemplateLevelTwoBaffleCalculationsService,
			EnquiryGlobalTemplateLevelTwoContainerLinerCalculationsService enquiryGlobalTemplateLevelTwoContainerLinerCalculationsService,
			EnquiryGlobalTemplateLevelTwoHoodBagCalculationsService enquiryGlobalTemplateLevelTwoHoodBagCalculationsService,
			EnquiryGlobalTemplateLevelTwoPlatenCalculationsService enquiryGlobalTemplateLevelTwoPlatenCalculationsService,
			EnquiryGlobalTemplateLevelTwoSingleLoopCalculationsService enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService,
			EnquiryGlobalTemplateLevelTwoTubularCalculationsService enquiryGlobalTemplateLevelTwoTubularCalculationsService,
			EnquiryGlobalTemplateLevelTwoTwoLoopCalculationsService enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService,
			EnquiryGlobalTemplateLevelTwoUPanelCalculationsService enquiryGlobalTemplateLevelTwoUPanelCalculationsService) {
		
		this.enquiryTemplateRepository = enquiryTemplateRepository;
		this.enquiryGlobalTemplateLevelOneProductCalculationsService = enquiryGlobalTemplateLevelOneProductCalculationsService;
		this.enquiryGlobalTemplateLevelOneFabricCalculationsService = enquiryGlobalTemplateLevelOneFabricCalculationsService;
		this.enquiryGlobalTemplateLevelOneTopcCalculationsService = enquiryGlobalTemplateLevelOneTopcCalculationsService;
		this.enquiryGlobalTemplateLevelOneBottomcCalculationsService = enquiryGlobalTemplateLevelOneBottomcCalculationsService;
		this.enquiryGlobalTemplateLevelOneLoopCalculationsService = enquiryGlobalTemplateLevelOneLoopCalculationsService;
		this.enquiryGlobalTemplateLevelOneOtherCalculationsService = enquiryGlobalTemplateLevelOneOtherCalculationsService;
		this.enquiryGlobalTemplateLevelTwo4PanelCalculationsService = enquiryGlobalTemplateLevelTwo4PanelCalculationsService;
		this.enquiryGlobalTemplateLevelTwoBaffleCalculationsService = enquiryGlobalTemplateLevelTwoBaffleCalculationsService;
		this.enquiryGlobalTemplateLevelTwoContainerLinerCalculationsService = enquiryGlobalTemplateLevelTwoContainerLinerCalculationsService;
		this.enquiryGlobalTemplateLevelTwoHoodBagCalculationsService = enquiryGlobalTemplateLevelTwoHoodBagCalculationsService;
		this.enquiryGlobalTemplateLevelTwoPlatenCalculationsService = enquiryGlobalTemplateLevelTwoPlatenCalculationsService;
		this.enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService = enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService;
		this.enquiryGlobalTemplateLevelTwoTubularCalculationsService = enquiryGlobalTemplateLevelTwoTubularCalculationsService;
		this.enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService = enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService;
		this.enquiryGlobalTemplateLevelTwoUPanelCalculationsService = enquiryGlobalTemplateLevelTwoUPanelCalculationsService;
	}
	
	//1. Perform All level one calculations
	private EnquiryTemplateBean performLevelOneCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		//1. Level one calculations
			enquiryTemplateBean = enquiryGlobalTemplateLevelOneCalculationsService.performLevelOneCalculations(enquiryTemplateBean);
		
		//2. Product : level one calc
			enquiryTemplateBean = enquiryGlobalTemplateLevelOneProductCalculationsService.performLevelOneCalculationsForProduct(enquiryTemplateBean);
		
		//3. Fabric : level one calc
			enquiryTemplateBean = enquiryGlobalTemplateLevelOneFabricCalculationsService.performLevelOneCalculationsForFabric(enquiryTemplateBean);
			
		//4. Top : level one calc
			enquiryTemplateBean = enquiryGlobalTemplateLevelOneTopcCalculationsService.performLevelOneCalculationsForTop(enquiryTemplateBean);
			
		//5. Bottom :  level one calc
			enquiryTemplateBean = enquiryGlobalTemplateLevelOneBottomcCalculationsService.performLevelOneCalculationsForBottom(enquiryTemplateBean);
			
		//6. Loop : level one calc
			enquiryTemplateBean = enquiryGlobalTemplateLevelOneLoopCalculationsService.performLevelOneCalculationsForLoop(enquiryTemplateBean);
			
		//7. Other : level one calc 
			enquiryTemplateBean = enquiryGlobalTemplateLevelOneOtherCalculationsService.performLevelOneCalculationsForOther(enquiryTemplateBean);
			
		//Shipment calculations :)
			
		return enquiryTemplateBean;
	}
	
	//2. Perform All level two calculations
		//2.1 Perform All level two 4 panel calculations
		private EnquiryTemplateBean performLevelTwo4PanelCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			//All level two calculations
			enquiryGlobalTemplateLevelTwo4PanelCalculationsService.calculateBagWeight(enquiryTemplateBean);
			enquiryGlobalTemplateLevelTwo4PanelCalculationsService.calculateBagCost(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//2.2 Perform All level two baffle panel calculations
		private EnquiryTemplateBean performLevelTwoBaffleCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			//All level two calculations
			enquiryGlobalTemplateLevelTwoBaffleCalculationsService.calculateBagWeight(enquiryTemplateBean);
			enquiryGlobalTemplateLevelTwoBaffleCalculationsService.calculateBagCost(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//2.3 Perform All level two container liner  calculations
		private EnquiryTemplateBean performLevelTwoContainerLinerCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			//All level two calculations
			enquiryGlobalTemplateLevelTwoContainerLinerCalculationsService.calculateBagWeight(enquiryTemplateBean);
			enquiryGlobalTemplateLevelTwoContainerLinerCalculationsService.calculateBagCost(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//2.4 Perform All level two hood bag  calculations
		private EnquiryTemplateBean performLevelTwoHoodBagCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			//All level two calculations
			enquiryGlobalTemplateLevelTwoHoodBagCalculationsService.calculateBagWeight(enquiryTemplateBean);
			enquiryGlobalTemplateLevelTwoHoodBagCalculationsService.calculateBagCost(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//2.5 Perform All level two platen bag  calculations
		private EnquiryTemplateBean performLevelTwoPlatenBagCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			//All level two calculations
			enquiryGlobalTemplateLevelTwoPlatenCalculationsService.calculateBagWeight(enquiryTemplateBean);
			enquiryGlobalTemplateLevelTwoPlatenCalculationsService.calculateBagCost(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//2.6 Perform All level two single loop calculations
		private EnquiryTemplateBean performLevelTwoSingleLoopCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			//All level two calculations
			enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService.calculateBagWeight(enquiryTemplateBean);
			enquiryGlobalTemplateLevelTwoSingleLoopCalculationsService.calculateBagCost(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//2.7 Perform All level two tubular calculations
		private EnquiryTemplateBean performLevelTwoTubularCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			//All level two calculations
			enquiryGlobalTemplateLevelTwoTubularCalculationsService.calculateBagWeight(enquiryTemplateBean);
			enquiryGlobalTemplateLevelTwoTubularCalculationsService.calculateBagCost(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//2.8 Perform All level two two loop calculations
		private EnquiryTemplateBean performLevelTwoTwoLoopCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			//All level two calculations
			enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService.calculateBagWeight(enquiryTemplateBean);
			enquiryGlobalTemplateLevelTwoTwoLoopCalculationsService.calculateBagCost(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//2.9 Perform All level two upanel calculations
		private EnquiryTemplateBean performLevelTwoUpanelCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			//All level two calculations
			enquiryGlobalTemplateLevelTwoUPanelCalculationsService.calculateBagWeight(enquiryTemplateBean);
			enquiryGlobalTemplateLevelTwoUPanelCalculationsService.calculateBagCost(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
	//3 Perform all calculations
		//3.1 Perform all 4 panel calculations
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
		
		//3.3 Perform all container liner calculations
		public EnquiryTemplateBean performAllContainerLinerCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
			enquiryTemplateBean = performLevelTwoContainerLinerCalculations(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//3.4 Perform all hood bag calculations
		public EnquiryTemplateBean performAllHoodBagCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
			enquiryTemplateBean = performLevelTwoHoodBagCalculations(enquiryTemplateBean);
			return enquiryTemplateBean;
		}
		
		//3.5 Perform all platen bag calculations
		public EnquiryTemplateBean performAllPlatenBagCalculations(EnquiryTemplateBean enquiryTemplateBean) {
			enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
			enquiryTemplateBean = performLevelTwoPlatenBagCalculations(enquiryTemplateBean);
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
	
	/*//2. Perform All level two calculations
	private EnquiryTemplateBean performLevelTwoCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		//All level two calculations
		enquiryGlobalTemplateLevelTwo4PanelCalculationsService.calculateBagWeight(enquiryTemplateBean);
		enquiryGlobalTemplateLevelTwo4PanelCalculationsService.calculateBagCost(enquiryTemplateBean);
		return enquiryTemplateBean;
	}
	
	//3. Perform all calculations
	public EnquiryTemplateBean performAllCalculations(EnquiryTemplateBean enquiryTemplateBean) {
		enquiryTemplateBean = performLevelOneCalculations(enquiryTemplateBean);
		enquiryTemplateBean = performLevelTwoCalculations(enquiryTemplateBean);
		return enquiryTemplateBean;
	}*/

	public EnquiryTemplateBean saveNewEnquiry(EnquiryTemplateBean enquiryBean) {
		return enquiryTemplateRepository.save(enquiryBean);
	}
	
	public EnquiryTemplateBean getEnquiryDetailsByEnquiryId(Long enquiryId) {
		return enquiryTemplateRepository.findByEnquiryId(enquiryId);
	}

	public EnquiryTemplateBean getEnquiryDetailsByEnquiryIdAndEnquiryNumber(Long enquiryId, String enquiryNumber) {
		return enquiryTemplateRepository.findByEnquiryIdAndEnquiryNumber(enquiryId, enquiryNumber);
	}

	public String emailGlobalTemplateEnquiryQuotation(Long enquiryId, String enquiryNumber, MailBean mailBean) {
		EnquiryTemplateBean enquiryTemplateBean = getEnquiryDetailsByEnquiryIdAndEnquiryNumber(enquiryId, enquiryNumber);
		return emailController.sendNewEnquiryQuotation(enquiryTemplateBean, mailBean);
	}
	
	public void downloadGlobalTemplateEnquiryQuotation(Long enquiryId, String enquiryNumber, HttpServletResponse response) {
		EnquiryTemplateBean enquiryTemplateBean = getEnquiryDetailsByEnquiryIdAndEnquiryNumber(enquiryId, enquiryNumber);
		pdfGeneratorUtilController.downloadNewEnquiryPdf(enquiryTemplateBean, response);
	}

	public ArrayList<EnquiryTemplateBean> getEnquiryHistoryForUser(User user) {
		return enquiryTemplateRepository.findByRequester(user);
	}
}
