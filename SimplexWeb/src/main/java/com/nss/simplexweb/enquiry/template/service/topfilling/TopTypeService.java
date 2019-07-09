package com.nss.simplexweb.enquiry.template.service.topfilling;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.topfilling.TopType;
import com.nss.simplexweb.enquiry.template.repository.topfilling.TopTypeRepository;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("topTypeService")
public class TopTypeService {

	@Autowired
	private TopTypeRepository topTypeRepository;
	
	public TopTypeService(TopTypeRepository topTypeRepository) {
		// TODO Auto-generated constructor stub
		this.topTypeRepository = topTypeRepository;
	}
	
	public List<TopType> getTopTypeList() {
		// TODO Auto-generated method stub
		return topTypeRepository.findAll();
	}
	
	public List<TopType> getTopType(){
		ArrayList<TopType> list = new ArrayList<>();
		list.add(topTypeRepository.findByTopTypeAbbr(ENQUIRY.TOP_TYPE_TOP_FLAP));
		list.add(topTypeRepository.findByTopTypeAbbr(ENQUIRY.TOP_TYPE_TOP_SKIRT_WITH_FLAP));
		return list;
	}
}
