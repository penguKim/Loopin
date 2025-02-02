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
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.WareareaDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Warearea;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.PrimaryService;
import com.itwillbs.c4d2412t3p1.service.UtilService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
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
		List<WareareaDTO> wareareaList = primaryDTO.getWareareaList();
		WarehouseFilterRequest filter = primaryDTO.getWarehouseFilter();
		Map<String, Object> response = new HashMap<>();
		
	    try {
	    	primaryService.insert_WAREHOUSE(warehouse, wareareaList);
	    	
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
	    	List<Warearea> wareareaList = primaryService.select_WAREAREA_list(warehouse_cd);
			System.out.println("wareareaList : " + wareareaList);
			response.put("wareareaList", wareareaList);
			
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
			response.put("msg", "창고 삭제 중 오류가 발생했습니다.");
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
	
	
	// 제품 관리 --------------------------------------------
	
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/product_list")
	public String product_list(Model model) {
	    
		List<Map<String, String>> warehouseList = primaryService.select_WAREHOUSE_code();
		
		model.addAttribute("warehouseList", warehouseList);
		
		return "/primary/product_list";
	}

	// 제품 조회
	@ResponseBody
	@PostMapping("/select_PRODUCT_list")
	public Map<String, Object> select_PRODUCT_list(@RequestBody PrimaryRequestDTO primaryDTO) {
		ProductFilterRequest filter = primaryDTO.getProductFilter();
		Map<String, Object> response = new HashMap<>(); 
		List<ProductDTO> list = primaryService.select_PRODUCT_list(filter);
		
		response.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		response.put("data", data);
		
		return response;
	}
	
	// 창고 등록
	// @LogActivity(value = "등록", action = "제품등록")
	@ResponseBody
	@PostMapping("/insert_PRODUCT")
	public ResponseEntity<Map<String, Object>> insert_PRODUCT(@RequestBody PrimaryRequestDTO primaryDTO) {
		ProductDTO product = primaryDTO.getProduct();
		List<String> sizeList = primaryDTO.getSizeList();
		List<String> colorList = primaryDTO.getColorList();
		ProductFilterRequest filter = primaryDTO.getProductFilter();
		
		System.out.println("사이즈는" + sizeList);
		System.out.println("컬러는" + colorList);
		Map<String, Object> response = new HashMap<>();
		
	    try {
	    	primaryService.insert_PRODUCT(product, sizeList, colorList);
	    	
			List<ProductDTO> list = primaryService.select_PRODUCT_list(filter);
			response.put("list", list);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "등록에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
}