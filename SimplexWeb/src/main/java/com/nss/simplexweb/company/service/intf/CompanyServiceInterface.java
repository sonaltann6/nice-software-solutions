package com.nss.simplexweb.company.service.intf;

import java.util.ArrayList;

import com.nss.simplexweb.company.model.Company;

public interface CompanyServiceInterface {

	//Create
		Company saveNewCompany(Company company);
		
	
	//Read
		ArrayList<Company> findAll();
		
		ArrayList<Company> findAllActiveCompanyList();
		
		Company findActiveCompanyByCompanyId(Long companyId);
		
		Company findActiveCompanyByCompanyUniqueCode(String companyUniqueCode);
	
		
	//Update
		Company updateCompany(Company company);
		
	//Delete
		Company inactivateCompany(Company company);
}
