package com.nss.simplexweb.enquiry.template.service.loop;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.loop.LoopMaterial;
import com.nss.simplexweb.enquiry.template.repository.loop.LoopMaterialRepository;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("loopMaterialService")
public class LoopMaterialService {

	@Autowired
	private LoopMaterialRepository loopMaterialRepository;
	
	public LoopMaterialService(LoopMaterialRepository loopMaterialRepository) {
		// TODO Auto-generated constructor stub
		this.loopMaterialRepository = loopMaterialRepository;
	}
	
	public List<LoopMaterial> getLoopMaterialList() {
		// TODO Auto-generated method stub
		return loopMaterialRepository.findAll();
	}
	
	public List<LoopMaterial> getABCLoopMaterialList() {
		List<LoopMaterial> list = new ArrayList<>();
		list.add(loopMaterialRepository.findByLoopMaterialAbbr(ENQUIRY.LOOP_MATERIAL_PP_LOOP));
		list.add(loopMaterialRepository.findByLoopMaterialAbbr(ENQUIRY.LOOP_MATERIAL_MULTIFILAMENT_LOOP));
		return list;
	}
}
