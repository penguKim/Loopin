package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.service.CommuteService;
import com.itwillbs.c4d2412t3p1.service.DashboardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class DashboardController {
	
	private final DashboardService dashboardService;
	private final CommuteService commuteService;

	@GetMapping("/login")
	public String login(HttpSession session, Model model) {
	    String errorMessage = (String) session.getAttribute("loginError");
	    if (errorMessage != null) {
	        model.addAttribute("errorMessage", errorMessage);
	        session.removeAttribute("loginError");
	    }
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
	
	@GetMapping("/select_EMPLOYEE_APPROVAL")
	public ResponseEntity<Map<String, Object>> select_EMPLOYEE_APPROVAL() {
		Map<String, Object> params = new HashMap<>();
		EmployeeDetails employee = commuteService.getEmployee();
		params.put("isAdmin", commuteService.isAuthority("SYS_ADMIN", "AT_ADMIN", "PR_ADMIN", "HR_ADMIN"));
		params.put("currentCd", employee.getEmployee_cd());
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			List<Map<String, Object>> approval = dashboardService.select_EMPLOYEE_APPROVAL(params);
			response.put("data", approval);
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}


	
	
}
