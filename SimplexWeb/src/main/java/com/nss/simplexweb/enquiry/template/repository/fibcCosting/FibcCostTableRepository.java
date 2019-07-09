package com.nss.simplexweb.enquiry.template.repository.fibcCosting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nss.simplexweb.enquiry.template.model.fibcCosting.FibcCostTable;

@Repository("fibcCostTableRepository")
public interface FibcCostTableRepository extends JpaRepository<FibcCostTable, Integer>{
	
	List<FibcCostTable> findByUserIdUserId(Long userId);
	
	Long removeByUserIdUserId(Long userId);
}
