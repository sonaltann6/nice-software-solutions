package com.nss.simplexweb.company.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.company.model.Company;
import com.nss.simplexweb.company.repository.CompanyRepository;
import com.nss.simplexweb.company.service.intf.CompanyServiceInterface;
import com.nss.simplexweb.utility.Utility;

@Service("companyService")
public class CompanyService implements CompanyServiceInterface {

	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	public CompanyService(
			CompanyRepository companyRepository) {
		// TODO Auto-generated constructor stub
		this.companyRepository = companyRepository;
	}
	
	@Override
	public Company saveNewCompany(Company company) {
		// TODO Auto-generated method stub
		company.setIsActive(1);
		return companyRepository.save(company);
	}

	@Override
	public ArrayList<Company> findAll() {
		// TODO Auto-generated method stub
		return companyRepository.findAll();
	}

	@Override
	public ArrayList<Company> findAllActiveCompanyList() {
		// TODO Auto-generated method stub
		return companyRepository.findByIsActive(1);
	}

	@Override
	public Company findActiveCompanyByCompanyId(Long companyId) {
		// TODO Auto-generated method stub
		return companyRepository.findByCompanyIdAndIsActive(companyId, 1);
	}

	@Override
	public Company findActiveCompanyByCompanyUniqueCode(String companyUniqueCode) {
		// TODO Auto-generated method stub
		return companyRepository.findByCompanyUniqueCodeAndIsActive(companyUniqueCode, 1);
	}

	@Override
	public Company updateCompany(Company company) {
		// TODO Auto-generated method stub
			Company oldCompanyData = companyRepository.findByCompanyIdAndIsActive(company.getCompanyId(), 1);
		
		//Fields that need not to be updated
			company.setCreatedBy(oldCompanyData.getCreatedBy());
			company.setCompanyUniqueCode(company.getCompanyUniqueCode());
			company.setIsActive(1);
			
		return companyRepository.save(company);
	}

	@Override
	public Company inactivateCompany(Company company) {
		// TODO Auto-generated method stub
		company.setIsActive(0);
		return companyRepository.save(company);
	}

	public String generateUniqueCompanyRegistrationCode() {
		// TODO Auto-generated method stub
			String uniqueCompanyRegistartionCode =  
					Utility.generateRandomPassword(3) +"-"+
							Utility.generateRandomPassword(3) +"-"+		
								Utility.generateRandomPassword(3);
		
		return uniqueCompanyRegistartionCode;
	}
}
