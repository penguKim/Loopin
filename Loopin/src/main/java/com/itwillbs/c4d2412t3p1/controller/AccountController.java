package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
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
		
		// 거래처유형
		model.addAttribute("ACtype_list", accountService.selectCommonList("ACTYPE"));

		// 사용여부
		model.addAttribute("useyn_list", accountService.selectCommonList("USEYN"));
		
		// 업태
		model.addAttribute("BTtype_list", accountService.selectCommonList("BTTYPE"));

		// 업종
		model.addAttribute("BItype_list", accountService.selectCommonList("BITYPE"));

		return "/account/account_list";
	}
	
	
	@GetMapping("/select_ACCOUNT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_ACCOUNT() {
		
		
		try {
	    	List<Map<String, Object>> response = accountService.select_ACCOUNT_DETAIL();
	    	return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	    
	    
	}
	
}
