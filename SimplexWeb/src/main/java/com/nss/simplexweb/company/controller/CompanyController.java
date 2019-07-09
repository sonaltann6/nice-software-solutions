package com.nss.simplexweb.company.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nss.simplexweb.SessionUtility;
import com.nss.simplexweb.company.model.Company;
import com.nss.simplexweb.company.service.impl.CompanyService;
import com.nss.simplexweb.enums.COMPANY;
import com.nss.simplexweb.enums.COUNTRY;
import com.nss.simplexweb.enums.PROJECT;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.CountryService;

@Controller
@RequestMapping("/master/company")
public class CompanyController {

	@Autowired
	CompanyService companyService;
	
	@Autowired
	private CountryService countryService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getCompanyList() {
		ModelAndView mav = new ModelAndView();
		
		List<Company> companyList = companyService.findAllActiveCompanyList();
		
		mav 
			.addObject(COMPANY.COMPANY_LIST.name(), companyList)
			.addObject(COUNTRY.COUNTRY_LIST.name(), countryService.getAllCountryList())
			.setViewName("master/company_master");
		return mav;
	}
	
	@RequestMapping(value="/saveNewCompany", method = RequestMethod.POST)
	public ModelAndView saveNewCompany(Company company, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		User currentUser = SessionUtility.getUserFromSession(request);
		company.setCreatedBy(currentUser);
		Company savedCompany = companyService.saveNewCompany(company);
		List<Company> companyList = companyService.findAllActiveCompanyList();
		
		mav 
			.addObject(COMPANY.COMPANY_LIST.name(), companyList)
			.addObject(COMPANY.COMPANY.name(), savedCompany)
			.setViewName("master/company_master");
		return mav;
	}
	
	@RequestMapping(value="/getCompanyDetailsById", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getCompanyDetails(@RequestParam("companyid") Long companyId) {
		Map<String, Object> map = new HashMap<>();
		Company savedCompany = companyService.findActiveCompanyByCompanyId(companyId);
		
		map.put(COMPANY.COMPANY.name(), savedCompany);
			
		return map;
	}
	
	@RequestMapping(value="/updateCompany", method = RequestMethod.POST)
	public @ResponseBody String updateCompany(Company company, ModelMap map) {
		Company updatedCompany = companyService.updateCompany(company);
		map
    		.addAttribute(PROJECT.SUCCESS_MSG.name(), "Company has been updated successfully")
    		.addAttribute(COMPANY.COMPANY.name(), updatedCompany);
	
		return map.toString();
	}
	
	@RequestMapping(value = "/deleteCompany", method = RequestMethod.GET)
	public @ResponseBody String deleteCompany(@RequestParam("companyid") Long companyid, ModelMap map) {
		Company company = companyService.findActiveCompanyByCompanyId(companyid);
        Company  deletedCompany = companyService.inactivateCompany(company);
        
        map
        	.addAttribute(PROJECT.SUCCESS_MSG.name(), "Company has been updated successfully")
        	.addAttribute(COMPANY.COMPANY.name(), deletedCompany);
		
		return map.toString();
	}
}
