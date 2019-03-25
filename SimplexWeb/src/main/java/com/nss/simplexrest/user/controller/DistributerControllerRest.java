package com.nss.simplexrest.user.controller;

import java.util.ArrayList;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nss.simplexrest.custom.exception.AlreadyExistsException;
import com.nss.simplexrest.custom.exception.NotFoundException;
import com.nss.simplexweb.enums.DISTRIBUTER;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.DistributerService;
import com.nss.simplexweb.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/master/distributerMaster")
@Api(value = "Distributer Resource REST Endpoint", description = "All Distributer Related Operations")
public class DistributerControllerRest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private DistributerService distributerService;

	// Get Distributer List Under Currently Logged in User
	@GetMapping(value = "/distributerList")
	public ArrayList<User> getAllDistributerList() {
		return distributerService.findAllActiveDistributersList();
	}

	// Create new Distributer
	@PostMapping(value = "/addNewDistributer")
	public User addNewDistributer(@RequestBody @Valid User user, BindingResult bindingResult) {
		User userExists = userService.findUserByEmailId(user.getEmail());
		if (userExists != null) {
			System.out.println("user exists");
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			new AlreadyExistsException(DISTRIBUTER.DISTRIBUTER.name(), user.getEmail(), null);
		} else {
			user = userService.processUseNameBeforeSaving(user);
			distributerService.saveNewDistributerAutoGeneratePassword(user);
			return user;
		}
		return null;
	}

	// Get Distributer Details By Id
	@GetMapping(value = "/getDistributerDetailsById/{userId}")
	@ApiOperation(value = "Returns Distributer details for given userId")
	public User getDistributerDetailsById(@PathVariable("userId") Long userId) {
		User user = userService.findUserByUserId(userId);
		if (user == null) {
			throw new NotFoundException("User", "UserId : " + userId, null);
		}
		return user;
	}

	// Update Distributer Details
	@PutMapping("/updateDistributer")
	
	public User updateDistributer(@RequestBody User user) {
		if (!userService.checkIfUserExistsByUserId(user.getUserId())) {
			throw new NotFoundException("User", "UserId : " + user.getUserId(), null);
		}
		User updatedUser = distributerService.updateDistributerWithoutPassword(user);
		return updatedUser;
	}

	// Delete Distributer
	@DeleteMapping("/deleteDistributer/{userId}")
	public User deleteDistributer(@PathVariable long userId) {
		return distributerService.inactivateDistributerById(userId);
	}

}
