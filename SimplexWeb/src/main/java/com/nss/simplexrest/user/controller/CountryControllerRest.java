package com.nss.simplexrest.user.controller;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.user.model.Country;
import com.nss.simplexweb.user.service.CountryService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/country")
@Api(value = "Country Resource REST Endpoint", description = "All Country Related Operations")
public class CountryControllerRest {

	
	@Autowired
	CountryService countryService;
	
	@GetMapping(value = "/getCountryList")
	public ArrayList<Country> getCountryList(){
		return countryService.getAllCountryList();
	}
}
