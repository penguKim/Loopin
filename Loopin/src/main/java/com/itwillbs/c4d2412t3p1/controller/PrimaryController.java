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
import com.itwillbs.c4d2412t3p1.domain.PrimaryRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.PrimaryService;
import com.itwillbs.c4d2412t3p1.service.UtilService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class PrimaryController {
	
	private final UtilService util;
	private final PrimaryService primaryService;
	private final CommonService commonService;
	

	// 창고 관리 --------------------------------------------
	
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/warehouse_list")
	public String warehouse_list(Model model) {
	    
		Map<String, List<Common_codeDTO>> commonList =  commonService.select_COMMON_list("USE", "USEYN", "WHTYPE");
		
		model.addAttribute("commonList", commonList);
		
		return "/primary/warehouse_list";
	}
	
	// 창고 조회
	@ResponseBody
	@PostMapping("/select_WAREHOUSE_list")
	public Map<String, Object> select_WAREHOUSE_list(@RequestBody PrimaryRequestDTO primaryDTO) {
		WarehouseFilterRequest filter = primaryDTO.getWarehouseFilter();
		System.out.println(filter);
		Map<String, Object> response = new HashMap<>(); 
		List<WarehouseDTO> list = primaryService.select_WAREHOUSE_list(filter);
		
		response.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		response.put("data", data);
		
		return response;
	}
	
	// 창고 등록
	@LogActivity(value = "등록", action = "창고등록")
	@ResponseBody
	@PostMapping("/insert_WAREHOUSE")
	public ResponseEntity<Map<String, Object>> insert_WAREHOUSE(@RequestBody PrimaryRequestDTO primaryDTO) {
		WarehouseDTO warehouse = primaryDTO.getWarehouse();
		WarehouseFilterRequest filter = primaryDTO.getWarehouseFilter();
		System.out.println("파라미터는 : " + warehouse);
		System.out.println("필터는 : " + filter);
		Map<String, Object> response = new HashMap<>();
		
	    try {
	    	primaryService.insert_WAREHOUSE(warehouse);
	    	
			List<WarehouseDTO> list = primaryService.select_WAREHOUSE_list(filter);
			response.put("list", list);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "등록에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 창고 상세 조회
	@ResponseBody
	@PostMapping("/select_WAREHOUSE_detail")
	public ResponseEntity<Map<String, Object>> select_WAREHOUSE_detail(@RequestBody PrimaryRequestDTO primaryDTO) {
		String warehouse_cd = primaryDTO.getWarehouse_cd();
		Map<String, Object> response = new HashMap<>();
		
	    try {
	    	Warehouse warehouse =  primaryService.select_WAREHOUSE_detail(warehouse_cd);
	    	response.put("warehouse", warehouse);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "조회에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 창고 삭제
	@ResponseBody
	@PostMapping("/delete_WAREHOUSE")
	public ResponseEntity<Map<String, Object>> delete_WAREHOUSE(@RequestBody PrimaryRequestDTO primaryDTO) {
		List<WarehouseDTO> warehouseList = primaryDTO.getWarehouseList();
		WarehouseFilterRequest filter = primaryDTO.getWarehouseFilter();
		Map<String, Object> response = new HashMap<>();
		
		try {
			primaryService.delete_WAREHOUSE(warehouseList);
			
			
			List<WarehouseDTO> list = primaryService.select_WAREHOUSE_list(filter);
			response.put("list", list);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "조회에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 창고코드 중복 체크
	@ResponseBody
	@PostMapping("/check_WAREHOUSE_CD")
	public Map<String, Object> check_WAREHOUSE_CD(@RequestBody PrimaryRequestDTO primaryDTO) {
		String warehouse_cd = primaryDTO.getWarehouse_cd();
		Map<String, Object> response = new HashMap<>();
		
		try {
			boolean check = primaryService.check_WAREHOUSE_CD(warehouse_cd); // 중복이면 false
			System.out.println("cpzmsms : " + check);
			response.put("result", check);
			if(!check) {
				response.put("title", "코드명 중복");
				response.put("msg", "이미 사용중인 코드입니다.");
			}
		} catch (Exception e) {
			response.put("msg", "체크에 실패했습니다.");
		}
		return response;
	}
	

	
}