package com.nss.simplexrest.user.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexweb.user.model.Role;
import com.nss.simplexweb.user.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/role")
@Api(value = "Role Resource REST Endpoint", description = "All Role List Operations")
public class RoleControllerRest {

	@Autowired
	private RoleService roleService;
	
	
	//Get All Roles List
	@GetMapping("/getAllRolesList")
	@ApiOperation(value = "Returns all roles list available")
	public List<Role> getAllRolesList(){
		return roleService.getAllRoleList();
	}
	
	//Get Roles List Under Employee
	/*@GetMapping("/getRoleListForEmployee/{userId}")
	@ApiOperation(value = "User Id is required"
	, notes = "Returns the role list for the given user Id")
	public List<Role> getRoleList(@NonNull @PathVariable long userId){
		User existingUser = userService.findUserByUserId(userId);
    	if (!userService.checkIfUserExists(existingUser))
    		throw new NotFoundException("User", "UserId : " + userId, null);
    	
		String CURRENT_USER_ROLE = existingUser.getRole().getRoleAbbr();
		if(CURRENT_USER_ROLE.equalsIgnoreCase(ROLE.DIR.name())) {
			return roleService.getAllEmployeeRoleList();
		}else {
			return roleService.getAllEmployeeRoleListExcludingAdminRole();
		}
	}*/
}
