package com.nss.simplexweb.documents.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.documents.model.DocumentTrackStatus;
import com.nss.simplexweb.documents.model.DocumentTrackingDetail;
import com.nss.simplexweb.documents.model.DocumentType;
import com.nss.simplexweb.documents.repository.DocumentTrackStatusRepository;
import com.nss.simplexweb.documents.repository.DocumentTrackingDetailRepository;
import com.nss.simplexweb.documents.repository.DocumentTypeRepository;

@Service("documentTrackingService")
public class DocumentTrackingService {

	@Autowired
	private DocumentTypeRepository documentTypeRepository;
	
	@Autowired
	private DocumentTrackStatusRepository documentTrackStatusRepository;
	
	@Autowired
	private DocumentTrackingDetailRepository documentTrackingDetailRepository;
	
	public DocumentTrackingDetail save(DocumentTrackingDetail documentTrackingDetail) {
		return documentTrackingDetailRepository.save(documentTrackingDetail);
	}
	
	public ArrayList<DocumentType> getAllDocumentTypeList(){
		return documentTypeRepository.findAll();
	}
	
	public ArrayList<DocumentTrackStatus> getAllDocumentTrackStatusList(){
		return documentTrackStatusRepository.findAll();
	}

	public ArrayList<DocumentTrackingDetail> getAllDocumentTrackingDetailList(){
		return documentTrackingDetailRepository.findAll();
	}
	
	public ArrayList<DocumentTrackingDetail> getDocTrackDetailByPartner(Long userId){
		return documentTrackingDetailRepository.findByPartnerUserId(userId);
	}
	
	public ArrayList<DocumentTrackingDetail> getListByStatus(Long statusId, Long userId){
		return documentTrackingDetailRepository.findByStatusDocTrackStatusIdAndPartnerUserId(statusId, userId);
	}
	
	public ArrayList<DocumentTrackingDetail> getListByStatusName(String statusName, Long userId){
		return documentTrackingDetailRepository.findByStatusDocTrackStatusAbbrAndPartnerUserId(statusName, userId);
	}
}

