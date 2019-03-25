package com.nss.simplexrest.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexrest.custom.exception.AlreadyExistsException;
import com.nss.simplexrest.custom.exception.NotFoundException;
import com.nss.simplexweb.enums.EMPLOYEE;
import com.nss.simplexweb.enums.ROLE;
import com.nss.simplexweb.user.model.Role;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.EmployeeService;
import com.nss.simplexweb.user.service.RoleService;
import com.nss.simplexweb.user.service.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/rest/master/employeeMaster")
@Api(value = "Employee Resource REST Endpoint", description = "All Employee related Operations")
public class EmployeeMasterControllerRest {

	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private RoleService roleService;

	// Get Employee List Under Currently Logged in User
	@GetMapping(value = "/employeeList/{userId}")
	public Map<String, Object> getEmployeeListByUserId(@PathVariable("userId") Long userId) {
		User currentUser = userService.findUserByUserId(userId);
		if(currentUser.getRole().getRoleAbbr().equalsIgnoreCase(ROLE.DIST.name())) {
			throw new NotFoundException("User", "UserId : " + userId, null);
		}else {
			HashMap<String, Object> map = new HashMap<>();
			map.put(EMPLOYEE.EMPLOYEE.name(), userService.findUserByUserId(userId)); 
			return map;
		}
	}
	
	//get employee role list
	@GetMapping(value = "/getEmployeeRoleList")
	public ArrayList<Role> getEmployeeRoleList(){
		return roleService.getAllEmployeeRoleList();
	}
	
	//get employee list
	@GetMapping(value = "/getEmployeeList")
	public ArrayList<User> getEmployeeList(@RequestParam ("userId") Long userId){
		return employeeService.findAllllEmployeesUnderRoleId(userService.findUserByUserId(userId).getRole().getRoleId());
	}
	
	//save
	@PostMapping(value = "/saveEmployee")
	public User saveEmployee(@RequestBody User user) {
		User currentUser = userService.findUserByUserId(user.getUserId());
		if(currentUser != null) {
			throw new AlreadyExistsException("User", "email : " + user.getEmail(), null);
		}else {
			user = userService.processUseNameBeforeSaving(user);
			return employeeService.saveNewEmployeeAutoGeneratePassword(user);
		} 
	}
	
	//update
	@PutMapping(value = "/updateEmployee")
	public User saveEditedEmployee(@RequestBody User user) {
        return employeeService.updateEmlpoyeeWithoutPassword(user);
	}
	
	//delete
	@DeleteMapping(value = "/deleteEmployee")
	public User deleteEmployee(@RequestParam ("userId") Long userId) {
        return employeeService.inActivateEmployeeById(userId);
	}
}
