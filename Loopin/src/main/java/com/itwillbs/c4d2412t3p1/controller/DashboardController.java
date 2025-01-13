package com.itwillbs.c4d2412t3p1.controller;

import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.service.DashboardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class DashboardController {
	
	private final DashboardService dashboardService;

	@GetMapping("/login")
	public String login() {
		return "/login";
	}
	
	@GetMapping("/")
	public String main(Model model) {
		EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	      
		String currentCd = employeeDetails.getEmployee_cd();
		log.info(currentCd + "이걸로 조회 가능");
		
		Map<String, Object> employee = dashboardService.select_total_EMPLOYEE(currentCd);
		
		for (Map.Entry<String, Object> entry : employee.entrySet()) {
	        model.addAttribute(entry.getKey(), entry.getValue());
	    }
		
		
		return "/index";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/login";
	}

	
	
}
