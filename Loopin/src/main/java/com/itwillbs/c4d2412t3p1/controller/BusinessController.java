package com.itwillbs.c4d2412t3p1.controller;


import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.service.BusinessService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class BusinessController {

	private final BusinessService businessService ;
	
	
	// 수주관리 페이지로 이동
	@GetMapping("/contract_list")
	public String contract_list(Model model) {
		
		// 수주상태
		model.addAttribute("status_list", businessService.selectCommonList("STATUS"));
		
		
		return "/business/contract_list";
	}
	
	// 제품 조회
	@GetMapping("/select_RPODUCT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_RPODUCT() {
		
		try {
	    	List<Map<String, Object>> response = businessService.select_RPODUCT();
	    	
	    	return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	    
	}

	// 거래처 조회
	@GetMapping("/select_ACCOUNT_CONTRACT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_ACCOUNT_CONTRACT() {
		
		try {
			List<Map<String, Object>> response = businessService.select_ACCOUNT_CONTRACT();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
		
	}
	
}
