package com.nss.simplexrest.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.company.model.Company;
import com.nss.simplexweb.company.service.impl.CompanyService;
import com.nss.simplexweb.enums.COMPANY;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/company")
@Api(value = "Company Resource REST Endpoint", description = "All Company Related Operations")
public class CompanyRestController {
	
	@Autowired
	CompanyService companyService;

	@GetMapping(value="/getAllActiveCompany")
	public Map<String, List<?>> getAllActiveCompany(){
		Map<String, List<?>> map = new HashMap<>();
		
		List<Company> companyList = companyService.findAllActiveCompanyList();
		map.put(COMPANY.COMPANY_LIST.name(), companyList);
		
		return map;
	}
}
