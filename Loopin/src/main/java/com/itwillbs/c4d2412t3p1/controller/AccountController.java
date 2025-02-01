package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.c4d2412t3p1.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class AccountController {
	
	private final AccountService accountService;
	
	
    // 거래처 등록 페이지로 이동
	@GetMapping("/account_list")
	public String account_list(Model model) {
		
		model.addAttribute("ACtype_list", accountService.selectCommonList("ACTYPE"));
		
		return "/account/account_list";
	}
}
