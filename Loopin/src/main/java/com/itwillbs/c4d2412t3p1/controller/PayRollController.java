package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.service.PRService;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;


@Log
@Controller
@RequiredArgsConstructor
public class PayRollController {

	private final PRService prS;
	
	@GetMapping("/checkSalary")
	public String checkSalary() {
		return "payroll/checkSalary";
	}
	
//	@GetMapping("/checkpr")
//	@ResponseBody
//	public List<Map<String,Object>> checkpr() {
//		List<Map<String, Object>> list = prS.selectpr();
		
//		Map<String,Object> response = new HashMap<>();
//		response.put("result", true);

//		Map<String,Object> data = new HashMap<>();
//		data.put("contents", list);
		
//		response.put("data", data);
		
//		return list;
//	}
	@GetMapping("/checkpr")
	@ResponseBody
	public List<Map<String,Object>> checkpr(@RequestParam("employee_cd") Long employee_cd) {
		
		List<Map<String, Object>> list = prS.selectpr(employee_cd);
		
		return list;
	}
	@GetMapping("/checkprmodal")
	@ResponseBody
	public List<Map<String,Object>> checkprmodal(@RequestParam("employee_cd") Long employee_cd, @RequestParam("pr_id") Long pr_id) {
		
		List<Map<String, Object>> list = prS.selectprmodal(pr_id, employee_cd);
		
		return list;
	}
	
	
}
