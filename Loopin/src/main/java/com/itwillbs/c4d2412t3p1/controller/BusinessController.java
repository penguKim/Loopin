package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class BusinessController {

	// 수주관리 페이지로 이동
	@GetMapping("/contract_list")
	public String contract_list(Model model) {
		
		return "/business/contract_list";
	}
	
	
	
}
