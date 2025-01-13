package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.service.PRService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class PRController {

	private final PRService prS;
	
	@GetMapping("/prsend")
	public String prsend() {
		return "payroll/sendPR";
	}
	
	@GetMapping("/getempandbs")
	@ResponseBody
	public List<Employee> getempandbs() {
		
		List<Employee> list = prS.select_empworklastmth();
		
		return list;
	}
	
//	@GetMapping("/getworkingtimeformth")
//	@ResponseBody
//	public List<Map<String, Object>> getworkingtimeformth(@RequestParam("employee_cdList") String employee_cds) {
//		
//		ObjectMapper objMapper = new ObjectMapper();
//		List<String> employee_cdList = null;
//		
//		List<Map<String, Object>> list = prS.select_worktimelastmth(employee_cdList);
//		
//		return list;
//	}
	
}
