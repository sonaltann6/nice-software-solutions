package com.nss.simplexrest.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.lang.NonNull;
import org.springframework.validation.BindingResult;
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
import com.nss.simplexweb.company.model.Company;
import com.nss.simplexweb.enums.PROJECT;
import com.nss.simplexweb.enums.USER;
import com.nss.simplexweb.notifications.service.NotificationService;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.DistributerService;
import com.nss.simplexweb.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/rest/user")
@Api(value = "User Resource REST Endpoint", description = "All User Related Operations")
public class UserControllerRest {

	@Autowired
    private UserService userService;
	
	@Autowired
    private DistributerService distributerService;
	
	@Autowired
	private NotificationService notificationService;

    @PostMapping(value="/login")
    @ApiOperation(value = "Returns user details to be persist in application scope"
    			, notes = "User model needs be sent for authentication")
    public User login(@RequestBody User user){
    	System.out.println("user : " + user);
        User userDetails = userService.findUserByEmailIdAndNonEncrPassword(user.getEmail(), user.getPassword());
    		if(!userService.checkIfUserExistsByUserId(userDetails.getUserId())) {
    			throw new NotFoundException("User", "email : " + user.getEmail(), null);
    		}
    		
    	return userDetails;
    }
    
    
    @SuppressWarnings("null")
	@PostMapping(value = "/registration")
    public User registerDistributer(@RequestBody @Valid User user, BindingResult bindingResult) {
        User userExists = userService.findUserByEmailId(user.getEmail());
        Company company = userService.checkCompanyCode(user.getCompany().getCompanyUniqueCode());
        
        if (userExists != null) {
        	throw new AlreadyExistsException("User", "email : " + user.getEmail(), null);
        }
        else if (company == null) {
        	throw new NotFoundException("Company", "Company Code : " +company.getCompanyUniqueCode(), null);
        }
        else {
        	user = userService.processUseNameBeforeSaving(user);
            user = distributerService.saveNewDistributerWithoutAutoGeneratePassword(user);
            notificationService.saveNewRegistrationNotification(user, 1);
        }
        return user;
    }

    
    @GetMapping(value="/getAllUsersList")
    public ArrayList<User> getAllUsersList() {
    	return userService.findAllActiveUsersList();
    }
    
    @GetMapping(value="/getUserProfileDetails/{userId}")
    public User getUserProfileDetails(@NonNull @PathVariable("userId") Long userId) {
    	User user = userService.findUserByUserId(userId);
        if (user == null) {
        	throw new NotFoundException("User", "UserId : " + userId, null);
        }
        return user;
    }
    
    /*@GetMapping(value="/getEmployeesUnderUser/{userId}")
    public ArrayList<User> getEmployeesUnderUser(@NonNull @PathVariable("userId") Long userId) {
    	return userService.getAllUsersImmediatelyUnderMe(userId);
    }*/
    
    
    /*@PutMapping("/updateUser/{userId}")
    public User updateUser(@RequestBody User user, @NonNull @PathVariable long userId) {
    	User existingUser = userService.findUserByUserId(userId);
    	if (!userService.checkIfUserExistsByUserId(userId))
    		throw new NotFoundException("User", "UserId : " + userId, null);
    	user.setUserId(userId);
    	user = userService.saveNewEmployee(user);
    	return user;
    }*/
    
    /*@DeleteMapping("/deleteUser/{userId}")
    public User deleteUser(@NonNull @PathVariable long userId) {
    	User existingUser = userService.findUserByUserId(userId);
    	if (!userService.checkIfUserExists(existingUser))
    		throw new NotFoundException("User", "UserId : " + userId, null);
    	else
    		userService.deleteUser(userId);
    	return existingUser;
    } */
    
    
    @GetMapping("/doesPasswordExist")
    @ApiOperation(value = "Returns boolean (true/false) for user existence"
	, notes = "user id of user whose password is to be checked")
    public boolean doesPasswordExist(@RequestParam ("userId") Long userId, @RequestParam String password) {
    	User existingUser = userService.findUserByUserId(userId);
    	if (!userService.checkIfUserExistsByUserId(userId))
    		throw new NotFoundException("User", "UserId : " + userId, null);
    	return userService.checkIfPasswordExists(existingUser, password);
    }
    
    @GetMapping("/doesEmailExist")
    @ApiOperation(value = "Returns boolean (true/false) for user existence"
	, notes = "user id of user whose password is to be checked")
    public boolean doesEmailExist(@RequestParam ("email") String email) {
    	if (!userService.checkIfUserExistsByEmailId(email))
    		throw new NotFoundException("User", "Email : " + email, null);
    		
    	return true;
    }
    
    /*@PutMapping("/updatePassword/{userId}/{password}")
    @ApiOperation(value = "Returns boolean (true/false) for user existence"
	, notes = "user id of user whose password is to be checked")
    public User updatePassword(@NonNull @PathVariable Long userId, @NonNull @PathVariable String password) {
    	User existingUser = userService.findUserByUserId(userId);
    	if (!userService.checkIfUserExists(existingUser))
    		throw new NotFoundException("User", "UserId : " + userId, null);
    	return userService.updatePassword(existingUser, password);
    }*/
    
    @PutMapping("/resetPassword")
    @ApiOperation(value = "Returns boolean (true/false) for user existence"
	, notes = "user id of user whose password is to be checked")
    public User uptadePassword(@RequestParam ("email") String email) {
    	User existingUser = userService.findUserByEmailId(email);
    	if (!userService.checkIfUserExistsByUserId(existingUser.getUserId()))
    		throw new NotFoundException("User", "Email : " + existingUser.getUserId(), null);
    	return userService.resetUserPassword(existingUser);
    }
    
    @PutMapping(value = "/changePassword")
    public Map<String,Object> changePassword(@RequestParam ("userId") Long userId, @RequestParam ("newPassword") String newPassword, HttpSession session) throws JSONException {
    	Map<String,Object> map = new HashMap<String, Object>();
    	User user  = userService.findUserByUserId(userId);
    	userService.changeUserPassword(user, newPassword);
    	map.put(PROJECT.SUCCESS_MSG.name(), "Please login again with new password");
    	map.put(USER.USER.name(), new User());
    	session.invalidate();
    	return map;
    }
}
