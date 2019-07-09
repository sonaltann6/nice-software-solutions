package com.nss.simplexweb.enquiry.template.service.other;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.other.DocPouchDetailType;
import com.nss.simplexweb.enquiry.template.repository.other.DocPouchDetailTypeRepository;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("docPouchDetailTypeService")
public class DocPouchDetailTypeService {

	@Autowired
	private DocPouchDetailTypeRepository docPouchDetailTypeRepository;
	
	public DocPouchDetailTypeService(DocPouchDetailTypeRepository docPouchDetailTypeRepository) {
		// TODO Auto-generated constructor stub
		this.docPouchDetailTypeRepository = docPouchDetailTypeRepository;
	}
	
	public List<DocPouchDetailType> getDocPouchDetailTypeList() {
		// TODO Auto-generated method stub
		return docPouchDetailTypeRepository.findAll();
	}
	
	public List<DocPouchDetailType> getABCDocPouchDetailTypeList() {
		List<DocPouchDetailType> list = new ArrayList<>();
		list.add(docPouchDetailTypeRepository.findByDocPouchDetailTypeAbbr(ENQUIRY.DOC_POUCH_DETAIL_TOP_SEAM));
		return list;
	}
}
