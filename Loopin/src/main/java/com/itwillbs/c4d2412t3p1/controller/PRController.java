package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PRController {

	@GetMapping("/prsend")
	public String prsend() {
		return "payroll/sendPR";
	}
	
}
