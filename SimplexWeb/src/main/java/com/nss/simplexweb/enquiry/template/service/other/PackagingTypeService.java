package com.nss.simplexweb.enquiry.template.service.other;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.other.PackagingType;
import com.nss.simplexweb.enquiry.template.repository.other.PackagingTypeRepository;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("packagingTypeService")
public class PackagingTypeService {

	@Autowired
	private PackagingTypeRepository packagingTypeRepository;

	public PackagingTypeService(PackagingTypeRepository packagingTypeRepository) {
		// TODO Auto-generated constructor stub
		this.packagingTypeRepository = packagingTypeRepository;
	}
	
	public List<PackagingType> getPackagingTypeList() {
		// TODO Auto-generated method stub
		return packagingTypeRepository.findAll();
	}
	
	public List<PackagingType> getABCPackagingTypeList() {
		List<PackagingType> list = new ArrayList<>();
		list.add(packagingTypeRepository.findByPackagingTypeAbbr(ENQUIRY.PACKAGING_TYPE_PALLET));
		return list;
	}
}
