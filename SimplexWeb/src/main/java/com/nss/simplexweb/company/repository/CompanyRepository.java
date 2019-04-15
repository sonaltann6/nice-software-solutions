package com.nss.simplexweb.company.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nss.simplexweb.company.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	//Read
		ArrayList<Company> findAll();
		
		ArrayList<Company> findByIsActive(int isActive);
		
		Company findByCompanyIdAndIsActive(Long companyId, int isActive);
		
		Company findByCompanyUniqueCodeAndIsActive(String companyUniqueCode, int isActive);
		
}
