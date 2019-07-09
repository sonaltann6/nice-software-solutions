package com.nss.simplexweb.enquiry.template.service.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.product.ProductModelType;
import com.nss.simplexweb.enquiry.template.repository.product.ProductModelTypeRepository;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("productModelTypeService")
public class ProductModelTypeService {

	@Autowired
	private ProductModelTypeRepository productModelTypeRepository;
	
	public ProductModelTypeService(ProductModelTypeRepository productModelTypeRepository) {
		this.productModelTypeRepository = productModelTypeRepository;
	}
	
	public List<ProductModelType> getProductModelTypeList() {
		// TODO Auto-generated method stub
		return productModelTypeRepository.findAll();
	}
	
	public List<ProductModelType> getABCProductModelTypeList(){
		List<ProductModelType> list = new ArrayList<>();
		list.add(productModelTypeRepository.findByModelTypeAbbr(ENQUIRY.MODEL_TYPE_4PANEL));
		list.add(productModelTypeRepository.findByModelTypeAbbr(ENQUIRY.MODEL_TYPE_UPANEL));
		list.add(productModelTypeRepository.findByModelTypeAbbr(ENQUIRY.MODEL_TYPE_TUBULAR_CIRCULAR));
		list.add(productModelTypeRepository.findByModelTypeAbbr(ENQUIRY.MODEL_TYPE_SINGLE_LOOP));
		list.add(productModelTypeRepository.findByModelTypeAbbr(ENQUIRY.MODEL_TYPE_TWO_LOOP));
		list.add(productModelTypeRepository.findByModelTypeAbbr(ENQUIRY.MODEL_TYPE_Q_BAFFLE));
		return list;	
	}
}
