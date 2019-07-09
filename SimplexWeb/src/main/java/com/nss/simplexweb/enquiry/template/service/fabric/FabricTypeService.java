package com.nss.simplexweb.enquiry.template.service.fabric;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.fabric.FabricType;
import com.nss.simplexweb.enquiry.template.repository.fabric.FabricTypeRepositoy;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("fabricTypeService")
public class FabricTypeService {

	@Autowired
	private FabricTypeRepositoy fabricTypeRepositoy;
	
	public FabricTypeService(FabricTypeRepositoy fabricTypeRepositoy) {
		// TODO Auto-generated constructor stub
		this.fabricTypeRepositoy = fabricTypeRepositoy;
	}
	
	public List<FabricType> getFabricTypeList() {
		// TODO Auto-generated method stub
		return fabricTypeRepositoy.findAll();
	}
	
	public List<FabricType> getABCFabricTypeList() {
		List<FabricType> list = new ArrayList<>();
		list.add(fabricTypeRepositoy.findByfabricTypeAbbr(ENQUIRY.FABRIC_TYPE_FLAT_FABRIC));
		list.add(fabricTypeRepositoy.findByfabricTypeAbbr(ENQUIRY.FABRIC_TYPE_TUBULAR_FABRIC));
		return list;
	}
}
