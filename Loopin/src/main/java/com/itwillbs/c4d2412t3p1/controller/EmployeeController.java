package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Member;
import com.itwillbs.c4d2412t3p1.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class EmployeeController {

	private final EmployeeService employeeService;
	
	
	@GetMapping("/employee_register")
	public String employee_register() {
		return "/employee/employee_register";
	}

	@GetMapping("/employee_list")
	public String employee_list(Model model) {
		
		List<Employee> employeeList = employeeService.findAll();
		model.addAttribute("employeeList", employeeList);
		
		
		return "/employee/employee_list";
	}

	@PostMapping("/employee_list2")
	public String employee_registerPro(EmployeeDTO employeeDTO) {

		log.info(employeeDTO.toString());
		
		employeeService.save(employeeDTO);
		
		return "redirect:/employee_list";
	}
	
	
}
