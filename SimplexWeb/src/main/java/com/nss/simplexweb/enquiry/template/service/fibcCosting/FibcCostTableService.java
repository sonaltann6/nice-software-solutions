package com.nss.simplexweb.enquiry.template.service.fibcCosting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.fibcCosting.FibcCostTable;
import com.nss.simplexweb.enquiry.template.model.fibcCosting.FibcItemCostTable;
import com.nss.simplexweb.enquiry.template.repository.fibcCosting.FibcCostTableRepository;
import com.nss.simplexweb.enquiry.template.repository.fibcCosting.FibcItemCostTableRepository;

@Service("fibcCostTableservice")
public class FibcCostTableService {
	
	@Autowired
	FibcCostTableRepository fibcCostTableRepository;
	
	@Autowired
	FibcItemCostTableRepository fibcItemCostTableRepository;
	
	public FibcCostTableService (FibcCostTableRepository fibcCostTableRepository) {
		this.fibcCostTableRepository = fibcCostTableRepository;
	}
	
	public List<FibcCostTable> getFibcCostTableList(){
		return fibcCostTableRepository.findAll();
	}
	
	/*public FibcCostTable findByUserId(Long userId) {
		return fibcCostTableRepository.findByUserIdUserId(userId);
	}*/
	
	public List<FibcCostTable> findAllByUserId(Long userId){
		return fibcCostTableRepository.findByUserIdUserId(userId);
	}
	
	public ArrayList<FibcItemCostTable> getFibcItemCost(){
		return fibcItemCostTableRepository.findAll();
	}
	
	public FibcCostTable save(FibcCostTable costTable) {
		return fibcCostTableRepository.save(costTable);
	}

	public void deleteOldEntryBySrNo(int srNo) {
		try {
			fibcCostTableRepository.deleteById(srNo);
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}
}
