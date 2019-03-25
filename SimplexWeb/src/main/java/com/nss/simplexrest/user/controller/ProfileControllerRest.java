package com.nss.simplexrest.user.controller;

import java.io.File;
import java.nio.file.Files;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexrest.custom.exception.NotFoundException;
import com.nss.simplexweb.enums.ROLE;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.DistributerService;
import com.nss.simplexweb.user.service.EmployeeService;
import com.nss.simplexweb.user.service.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/profile")
@Api(value = "Profile Resource REST Endpoint", description = "All Profile Related Operations")
public class ProfileControllerRest {

	@Autowired
	UserService userService;
	
	@Autowired
	DistributerService distributerService;
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@GetMapping(value = "/getProfileData/{userId}")
	public User getProfileData(@PathVariable ("userId") Long userId) {
		User currentUser = userService.findUserByUserId(userId);
		if(currentUser.getRole().getRoleAbbr().equalsIgnoreCase(ROLE.DIST.name())) {
			return distributerService.findDistributerByUserId(userId);
		}else {
			Long employeeId = currentUser.getEmpId();
			return employeeService.findEmployeeByEmployeeId(employeeId);
		}
	}
	
	@PutMapping(value = "/updateProfileData")
	public User updateProfileData(@RequestBody User user) {
		if(!userService.checkIfUserExistsByUserId(user.getUserId())) {
			throw new NotFoundException("User", "email : " + user.getEmail(), null);
		}
		if(user.getRole().getRoleAbbr().equalsIgnoreCase(ROLE.DIST.name())) {
			return distributerService.updateDistributerWithoutPassword(user);
		}else {
			return employeeService.updateEmlpoyeeWithoutPassword(user);
		}
	}
	
	@GetMapping(value = "/getProfilePicture")
	public @ResponseBody String getProfilePicture(@RequestParam ("userId") Long userId) {
		String filepath = null;
		JSONObject obj = new JSONObject();
		Resource fileResource = resourceLoader.getResource("classpath:static/img/default_profile_img.jpg");
		
		try {
			User user = userService.findUserByUserId(userId);
			if(user.getProfilePicFolderpath()!=null && user.getProfilePicFilename()!=null) {
	    		filepath = user.getProfilePicFolderpath()+ File.separator + user.getProfilePicFilename();
	    	}
			if(filepath != null) {
	    		File file = new File(filepath);
		    	if(file.exists() && file.isFile()) {
		    		obj.put("IMAGE", Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
		    		//return Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath()));
		    	}else {
		    		obj.put("IMAGE", Base64.getEncoder().encodeToString(Files.readAllBytes(fileResource.getFile().toPath())));
		    		//return Base64.getEncoder().encodeToString(Files.readAllBytes(fileResource.getFile().toPath()));
		    	}
	    	}else {
	    		obj.put("IMAGE", Base64.getEncoder().encodeToString(Files.readAllBytes(fileResource.getFile().toPath())));
	    		//return Base64.getEncoder().encodeToString(Files.readAllBytes(fileResource.getFile().toPath()));
	    	}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	@PostMapping(value = "/setProfilePicture")
	@ResponseBody
	public User setProfilePicture(@RequestBody String base64Image, @RequestParam ("userId") Long userId) {
		User user = userService.findUserByUserId(userId);
		return userService.uploadMyProfilePicture(base64Image, user);
	}
}
