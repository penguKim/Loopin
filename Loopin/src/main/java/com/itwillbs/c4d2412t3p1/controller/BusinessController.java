package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class BusinessController {

	// 발주관리 페이지로 이동
	@GetMapping("/order_list")
	public String order_list() {
		
		return "/business/order_list";
	}
	
	
	
}
