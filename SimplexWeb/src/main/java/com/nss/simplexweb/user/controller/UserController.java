package com.nss.simplexweb.user.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nss.simplexweb.SessionUtility;
import com.nss.simplexweb.company.model.Company;
import com.nss.simplexweb.enums.COUNTRY;
import com.nss.simplexweb.enums.NOTIFICATION;
import com.nss.simplexweb.enums.PROJECT;
import com.nss.simplexweb.enums.USER;
import com.nss.simplexweb.notifications.service.NotificationService;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.repository.CountryRepository;
import com.nss.simplexweb.user.service.DistributerService;
import com.nss.simplexweb.user.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private DistributerService distributerService;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private NotificationService notificationService;

	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("login");
		return mav;
	}

	@RequestMapping(value = { "/registration" }, method = RequestMethod.GET)
	public ModelAndView register() {
		ModelAndView mav = new ModelAndView();
		User user = new User();
		mav.addObject(COUNTRY.COUNTRY_LIST.name(), countryRepository.findAllByOrderByCountryNameAsc())
				.addObject(USER.USER.name(), user).setViewName("registration");
		return mav;
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
		ModelAndView mav = new ModelAndView();
		User userExists = userService.findUserByEmailId(user.getEmail());
		Company company = userService.checkCompanyCode(user.getCompany().getCompanyUniqueCode());

		if (userExists != null) {
			System.out.println("user exists");
			bindingResult.rejectValue("email", "error.user",
					"There is already a user registered with the email provided");
			mav
				.addObject(PROJECT.ERROR_MSG.name(), "User already exists!!");
			
		} else if (company == null) {
			System.out.println("company code does not exist");
			bindingResult.rejectValue("company.companyUniqueCode", "error.user",
					"Company code does not exist");
			mav
				.addObject(PROJECT.ERROR_MSG.name(), "Company code does not exist!!");
		}

		if (bindingResult.hasErrors()) {
			mav
				.addObject(USER.USER.name(), new User())
				.setViewName("registration");
		} else {
			user = userService.processUseNameBeforeSaving(user);
			distributerService.saveNewDistributerWithoutAutoGeneratePassword(user);
			notificationService.saveNewRegistrationNotification(user, 1);
			mav.addObject(PROJECT.SUCCESS_MSG.name(), "User has been registered successfully")
					.addObject(USER.USER.name(), new User()).setViewName("login");
		}
		return mav;
	}

	@RequestMapping(value = { "/forgotPassword" }, method = RequestMethod.GET)
	public ModelAndView forgotPassword() {
		ModelAndView mav = new ModelAndView();
		mav.addObject(USER.USER.name(), new User()).setViewName("forgot_password");
		return mav;
	}

	@RequestMapping(value = { "/forgotPassword" }, method = RequestMethod.POST)
	public ModelAndView generateNewPassword(User user) {
		ModelAndView mav = new ModelAndView();
		User userExists = userService.findUserByEmailIdAndIsActive(user.getEmail());
		if (userExists != null) {
			userService.resetUserPassword(userExists);
			mav.addObject(PROJECT.RET_MSG.name(), "New password sent to registered email");
		} else {
			mav.addObject(PROJECT.RET_MSG.name(), "Invaid email, kindly register first");
		}
		mav.addObject(USER.USER.name(), new User()).setViewName("forgot_password");
		return mav;
	}

	@RequestMapping(value = { "user/home" }, method = RequestMethod.GET)
	public ModelAndView userHome(HttpSession session, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("home");
		return mav;
	}

	@RequestMapping(value = { "/changeMyPassword" }, method = RequestMethod.GET)
	public ModelAndView changeMyPassword(HttpSession session) {
		ModelAndView mav = new ModelAndView();

		mav.setViewName("home");
		return mav;
	}

	@RequestMapping(value = { "/notifications" }, method = RequestMethod.GET)
	public ModelAndView getNotifications(HttpSession session, HttpServletRequest request)
			throws JsonProcessingException {
		User user = SessionUtility.getUserFromSession(request);
		ModelAndView mav = new ModelAndView();
		ArrayList<List<?>> list = new ArrayList<List<?>>();
		list = notificationService.getNotificationByGroup(user.getUserId());
		mav.addObject(NOTIFICATION.NOTIFICATION_LIST.name(), list).setViewName("notifications.html");
		// System.out.println(list.toString());
		return mav;
	}

	@RequestMapping(value = { "/checkIfPasswordExists" }, method = RequestMethod.GET)
	@ResponseBody
	public boolean checkIfPasswordExists(String currentPassword, HttpServletRequest request) {
		User user = SessionUtility.getUserFromSession(request);
		return userService.checkIfPasswordExists(user, currentPassword);
	}

	@RequestMapping(value = { "/changeMyPassword" }, method = RequestMethod.POST)
	public ModelAndView changeMyPasswordPost(String newPassword, HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		User user = SessionUtility.getUserFromSession(request);
		userService.changeUserPassword(user, newPassword);
		mav.addObject(PROJECT.SUCCESS_MSG.name(), "Please login again with new password")
				.addObject(USER.USER.name(), new User()).setViewName("login");

		// **important
		session.invalidate();
		return mav;
	}

}
