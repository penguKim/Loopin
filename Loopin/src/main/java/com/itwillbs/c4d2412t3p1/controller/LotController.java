package com.itwillbs.c4d2412t3p1.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.EquipmentDTO;
import com.itwillbs.c4d2412t3p1.domain.EquipmentRequestDTO;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.LotService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.EquipmentFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class LotController {

	private final CommonService commonService;
	private final LotService lotService;
	
	// 로트추적 페이지
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/lot_list")
	public String lot_list(Model model) {
	    
		Map<String, List<Common_codeDTO>> commonList = commonService.select_COMMON_list("USE", "USEYN", "PRDTYPE");
		List<Map<String, Object>> processList = lotService.select_PROCESS_list();
		model.addAttribute("processList", processList);
		model.addAttribute("commonList", commonList);
		
		return "/lot/lot_list";
	}
	
	// 생산실적 페이지
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/product_result")
	public String product_result(Model model) {
		
		Map<String, List<Common_codeDTO>> commonList = commonService.select_COMMON_list("USE", "USEYN", "PRDTYPE");
		List<Map<String, Object>> accountList = lotService.select_ACCOUNT_list();
		model.addAttribute("accountList", accountList);
		model.addAttribute("commonList", commonList);
		return "/lot/product_result";
	}
	
	// 로트추적 조회
	@ResponseBody
	@PostMapping("/select_LOT_list")
	public ResponseEntity<Map<String, Object>> select_LOT_list(@RequestBody(required = false) Map<String, Object> params) {
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			
			List<Map<String, Object>> lots = lotService.select_LOT_list(params);
			response.put("data", lots);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 로트차트 조회
	@ResponseBody
	@PostMapping("/select_LOT_json")
	public ResponseEntity<Map<String, Object>> select_LOT_json(@RequestBody Map<String, Object> params) {
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			
			Map<String, Object> JSON_DATA = lotService.select_LOT_json(params);
			response.put("data", JSON_DATA);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 생산실적 조회
	@ResponseBody
	@PostMapping("/select_RESULT_list")
	public ResponseEntity<Map<String, Object>> select_RESULT_list(@RequestBody(required = false) Map<String, Object> params) {
		
		log.info("params" + params);
		Map<String, Object> response = new HashMap<>(); 
		try {
			
			List<Map<String, Object>> results = lotService.select_RESULT_list(params);
			response.put("data", results);
			log.info("results" + response);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
	
	
}