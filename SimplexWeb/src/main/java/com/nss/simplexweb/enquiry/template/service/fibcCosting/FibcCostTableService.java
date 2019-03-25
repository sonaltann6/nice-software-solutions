package com.nss.simplexweb.enquiry.template.service.fibcCosting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.fibcCosting.FibcCostTable;
import com.nss.simplexweb.enquiry.template.repository.fibcCosting.FibcCostTableRepository;

@Service("fibcCostTableservice")
public class FibcCostTableService {
	
	@Autowired
	FibcCostTableRepository fibcCostTableRepository;
	
	public FibcCostTableService (FibcCostTableRepository fibcCostTableRepository) {
		this.fibcCostTableRepository = fibcCostTableRepository;
	}
	
	public List<FibcCostTable> getFibcCostTableList(){
		return fibcCostTableRepository.findAll();
	}
	
	public FibcCostTable findByUserId(Long userId) {
		return fibcCostTableRepository.findByUserId(userId);
	}
	
	public List<FibcCostTable> findAllByUserId(Long userId){
		return fibcCostTableRepository.findAllByUserId(userId);
	}
}
