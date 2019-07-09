package com.nss.simplexweb.documents.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nss.simplexweb.documents.model.DocumentTrackingDetail;

@Repository("documentTrackingDetailRepository")
public interface DocumentTrackingDetailRepository extends JpaRepository<DocumentTrackingDetail, Long>{

	//findAll
	ArrayList<DocumentTrackingDetail> findAll();
	
	//find by partner
	ArrayList<DocumentTrackingDetail> findByPartnerUserId(Long userId);
	
	//find by status
	ArrayList<DocumentTrackingDetail> findByStatusDocTrackStatusIdAndPartnerUserId(Long docTrackStatusId, Long userId);
	
	ArrayList<DocumentTrackingDetail> findByStatusDocTrackStatusAbbrAndPartnerUserId(String docTrackStatusAbbr, Long userId);
}
