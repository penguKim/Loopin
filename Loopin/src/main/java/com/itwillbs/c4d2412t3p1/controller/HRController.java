package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class HRController {

	@GetMapping("/HR_register")
	public String HR_register() {
		return "/HR_register";
	}
	
	@GetMapping("/registerSuccess")	
	public String success() {
		return "/success";
	}
}
