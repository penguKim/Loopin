package com.itwillbs.c4d2412t3p1.controller;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.service.CommuteService;
import com.itwillbs.c4d2412t3p1.service.DashboardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class DashboardController {
	
	private final DashboardService dashboardService;
	private final CommuteService commuteService;

	@GetMapping("/login")
	public String login() {
		return "/login";
	}
	
	@GetMapping("/")
	public String main(Model model) {
		
		EmployeeDetails employeeDetails = commuteService.getEmployee();
		String employee_cd = employeeDetails.getEmployee_cd();
		String workinghour_id = employeeDetails.getWorkinghour_id();
		Commute commute = commuteService.findById(employee_cd, workinghour_id);
		
		String currentCd = employeeDetails.getEmployee_cd();
		
		Map<String, Object> employee = dashboardService.select_total_EMPLOYEE(currentCd);
		
		for (Map.Entry<String, Object> entry : employee.entrySet()) {
	        model.addAttribute(entry.getKey(), entry.getValue());
	    }
		
		
	    boolean isAttendance = commute != null && 
                commute.getCommute_wt() != null && 
                !commute.getCommute_wt().isEmpty();
	    
	    boolean isLeaveWork = !isAttendance || 
               (commute != null && 
                commute.getCommute_lt() != null && 
                !commute.getCommute_lt().isEmpty());
	    
	    model.addAttribute("isAttendance", isAttendance);
	    model.addAttribute("isLeaveWork", isLeaveWork);
		
		return "/index";
	}


	
	
}
