package com.nss.simplexweb.enquiry.template.service.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nss.simplexweb.enquiry.template.model.product.ProductunitTypeWeight;
import com.nss.simplexweb.enquiry.template.repository.product.ProductUnitTypeWeightRepository;
import com.nss.simplexweb.enums.ENQUIRY;

@Service("productUnitTypeWeightService")
public class ProductUnitTypeWeightService {

	@Autowired
	private ProductUnitTypeWeightRepository productUnitTypeWeightRepository;
	
	public ProductUnitTypeWeightService(ProductUnitTypeWeightRepository productUnitTypeWeightRepository) {
		// TODO Auto-generated constructor stub
		this.productUnitTypeWeightRepository = productUnitTypeWeightRepository;
	}
	
	public List<ProductunitTypeWeight> getProductUnitTypeWeightList() {
		// TODO Auto-generated method stub
		return productUnitTypeWeightRepository.findAll();
	}
	
	public List<ProductunitTypeWeight> getABCProductUnitTypeWeightList(){
		List<ProductunitTypeWeight> list = new ArrayList<>();
		list.add(productUnitTypeWeightRepository.findByUnitTypeAbbr(ENQUIRY.UNIT_OUNCES));
		return list;
	}
}
