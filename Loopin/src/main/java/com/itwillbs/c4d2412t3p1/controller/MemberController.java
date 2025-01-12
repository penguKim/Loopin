package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.MemberDTO;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.service.CommuteService;
import com.itwillbs.c4d2412t3p1.service.MemberRepService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class MemberController {

	private final MemberRepService memberRepService;
	private final CommuteService commuteService;
	
	@GetMapping("/main")
	public String main(Model model) {
		
		EmployeeDetails employeeDetails = commuteService.getEmployee();
		String employee_cd = employeeDetails.getEmployee_cd();
		String workinghour_id = employeeDetails.getWorkinghour_id();
		Commute commute = commuteService.findById(employee_cd, workinghour_id);
		
	    boolean isAttendance = commute != null && 
                commute.getCommute_wt() != null && 
                !commute.getCommute_wt().isEmpty();
	    
	    boolean isLeaveWork = !isAttendance || 
               (commute != null && 
                commute.getCommute_lt() != null && 
                !commute.getCommute_lt().isEmpty());
	    // 출퇴근버튼 활성화 여부(대시보드로 로직 이동)
	    model.addAttribute("isAttendance", isAttendance);
	    model.addAttribute("isLeaveWork", isLeaveWork);
		
		return "/main";
	}
	
	@GetMapping("/login")
	public String login() {
		return "/login";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/login";
	}
	
	@GetMapping("/insert")
	public String insert() {
		log.info("get insert");
		return "/insert";
	}
	
	@PostMapping("/insert")
	public String insertPro(MemberDTO memberDTO) {
		log.info("post insert");
		log.info(memberDTO.toString());

		memberRepService.save(memberDTO);
		
		return "redirect:/login";
	}
	
	
	
}
