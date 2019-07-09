package com.nss.simplexweb.enquiry.template.repository.fibcCosting;

import java.util.ArrayList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nss.simplexweb.enquiry.template.model.fibcCosting.FibcItemCostTable;

@Repository("fibcItemCostTableRepository")
public interface FibcItemCostTableRepository extends JpaRepository<FibcItemCostTable, Long>{

	ArrayList<FibcItemCostTable> findAll();
} 