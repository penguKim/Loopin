package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.entity.PRCode;
import com.itwillbs.c4d2412t3p1.service.PRService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class PRController {

	private final PRService prS;
	
	@GetMapping("/prcode")
	public String prcode() {
		return "payroll/prcode";
	}
	
	@GetMapping("/getprcode")
	@ResponseBody
	public List<PRCode> getprcode() {
		
		List<PRCode> list = prS.getprcode();
		
		return list;
	}
	
}
