package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;

@Log
@Controller
@RequiredArgsConstructor
public class PayRollController {

	@GetMapping("/checkSalary")
	public String checkSalary() {
		return "/checkSalary";
	}
	
}
