package com.itwillbs.c4d2412t3p1.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.BomProcessDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeListDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanSaveRequest;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseListDTO;
import com.itwillbs.c4d2412t3p1.entity.BomProcess;
import com.itwillbs.c4d2412t3p1.service.ProductplanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Log
@Controller
public class ProductPlanController {

	private final ProductplanService productplanService;

	@GetMapping("/product_plan_list")
	public String product_plan() {

		return "productplan/productplan_list";
	}

	@GetMapping("/select_CONTRACTCD_list")
	@ResponseBody
	public ResponseEntity<List<ContractDetailDTO>> select_CONTRACTCD_list(
			@RequestParam(value = "contract_cd", required = false) String contract_cd) {
		List<ContractDetailDTO> contractDetails = productplanService.select_CONTRACTCD_list(contract_cd);
		log.info("수주바디 내용!!" + contractDetails.toString());
		return ResponseEntity.ok(contractDetails);
	}

	@GetMapping("/select_EMPLOYEE_list")
	public ResponseEntity<List<EmployeeListDTO>> select_EMPLOYEE_list(
			@RequestParam(value = "employee_cd", required = false) String employee_cd) {
		List<EmployeeListDTO> employeeList = productplanService.select_EMPLOYEE_BY_DP(employee_cd);
		return ResponseEntity.ok(employeeList);
	}
	
	@GetMapping("/select_WAREHOUSE_list")
	public ResponseEntity<List<WarehouseListDTO>> select_WAREHOUSE_list(
			@RequestParam(value = "warehouse_cd", required = false) String warehouse_cd) {
		List<WarehouseListDTO> list = productplanService.select_WAREHOUSE_BY_TP(warehouse_cd);
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/select_BOMPROCESS_list")
    public ResponseEntity<List<BomProcessDTO>> select_BOMPROCESS_list(
            @RequestParam(value = "product_cd", required = false) String product_cd
    ) {
		 if (product_cd == null || product_cd.trim().isEmpty()) {
	            return ResponseEntity.ok(Collections.emptyList());
	        }

	        // Service 호출 -> DTO 리스트 반환
	        List<BomProcessDTO> dtoList = productplanService.select_BOMPROCESS_BY_CD(product_cd);
	        return ResponseEntity.ok(dtoList);
    }
	
	@PostMapping("/save_PRODUCTPLAN")
	public ResponseEntity<?> saveProcessOrder(@RequestBody ProductPlanSaveRequest req) {
	    productplanService.save_PRODUCTPLAN(req);
	    return ResponseEntity.ok().body("SUCCESS");
	}
	
}
