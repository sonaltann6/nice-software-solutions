package com.nss.simplexweb.enquiry.template.controller.fibcCosting;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.nss.simplexweb.SessionUtility;
import com.nss.simplexweb.enquiry.template.model.fibcCosting.FibcCostTable;
import com.nss.simplexweb.enquiry.template.model.fibcCosting.FibcItemCostTable;
import com.nss.simplexweb.enquiry.template.service.fibcCosting.FibcCostTableService;
import com.nss.simplexweb.enums.DISTRIBUTER;
import com.nss.simplexweb.enums.FIBC;
import com.nss.simplexweb.enums.ROLE;
import com.nss.simplexweb.user.model.User;
import com.nss.simplexweb.user.service.DistributerService;

@Controller
@RequestMapping(value={"/fibc/costing"})
public class FIBCCostingController {
	
	@Autowired
	FibcCostTableService fibcCostTableService;
	
	@Autowired
	DistributerService distributerService;
	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView getFibcCosting(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		User currentUser = SessionUtility.getUserFromSession(request);
		ArrayList<User> distributerList = null;
		ArrayList<FibcItemCostTable> itemCostList = null;
		
		if(currentUser.getRole().getRoleAbbr().equals(ROLE.DIST.name())) {
			return mav;
		}else {
			distributerList = distributerService.findAllActiveDistributersList();
			itemCostList = fibcCostTableService.getFibcItemCost();
		}
		mav
			.addObject(DISTRIBUTER.DISTRIBUTER_LIST.name(),distributerList)
			.addObject(FIBC.FIBC_ITEM.name(),itemCostList)
			.addObject(FIBC.FIBC_COST.name(), new FibcCostTable())
			.setViewName("fibcCosting");
		return mav;
	}

	@RequestMapping(value="/getByUser", method = RequestMethod.GET)
	@ResponseBody
	public List<FibcCostTable> getByUser(@RequestParam("userId") Long userId) {
		List<FibcCostTable> itemCostList = fibcCostTableService.findAllByUserId(userId);
		return itemCostList;
	}
	
	@RequestMapping(value="/saveFibcCost", method = RequestMethod.POST)
	public ModelAndView saveFibcCost(@RequestBody List<FibcCostTable> fibcCostTable, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView();
		for(FibcCostTable costTable : fibcCostTable) {
			try {
				fibcCostTableService.deleteOldEntryBySrNo(costTable.getSrNo());
			}catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			fibcCostTableService.save(costTable);
		}
		mav
			.setViewName("fibcCosting");
		return mav;
	}
}
