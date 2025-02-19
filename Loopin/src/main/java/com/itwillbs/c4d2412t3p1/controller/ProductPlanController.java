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

import com.itwillbs.c4d2412t3p1.domain.BomMaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.BomProcessDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeListDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanProcessDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanSaveRequest;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanSaveRequest.ProcessOrderDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseListDTO;
import com.itwillbs.c4d2412t3p1.entity.BomProcess;
import com.itwillbs.c4d2412t3p1.entity.Productplan;
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

	@GetMapping("/select_PRODUCTPLAN_list")
	public ResponseEntity<List<Map<String, Object>>> select_PRODUCTPLAN_list() {
		List<Map<String, Object>> productPlans = productplanService.select_PRODUCTPLAN_list();
		return ResponseEntity.ok(productPlans);
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
			@RequestParam(value = "product_cd", required = false) String product_cd) {
		if (product_cd == null || product_cd.trim().isEmpty()) {
			return ResponseEntity.ok(Collections.emptyList());
		}

		// Service 호출 -> DTO 리스트 반환
		List<BomProcessDTO> dtoList = productplanService.select_BOMPROCESS_BY_CD(product_cd);
		return ResponseEntity.ok(dtoList);
	}

	@GetMapping("/select_PRODUCTPLANPROCESS_list")
	@ResponseBody
	public ResponseEntity<List<ProcessOrderDTO>> selectPRODUCTPLANPROCESSList(
			@RequestParam(value = "contract_cd", required = false) String contract_cd,
			@RequestParam(value = "product_cd", required = false) String product_cd) {

		List<ProcessOrderDTO> processList = productplanService.selectPRODUCTPLANPROCESSList(contract_cd, product_cd);
		return ResponseEntity.ok(processList);
	}

	@GetMapping("/select_BOMMATERIALS")
	public ResponseEntity<List<BomMaterialDTO>> selectBOMMaterials(
			@RequestParam(value = "product_cd", required = false) String productCd) {

		List<BomMaterialDTO> dtoList = productplanService.findAllMaterialsForProduct(productCd);
		return ResponseEntity.ok(dtoList);
	}

	@PostMapping("/save_PRODUCTPLAN")
	public ResponseEntity<Map<String, String>> saveProcessOrder(@RequestBody ProductPlanSaveRequest req) {
		productplanService.save_PRODUCTPLAN(req);
		Map<String, String> response = new HashMap<>();
		response.put("status", "SUCCESS");

		return ResponseEntity.ok(response);
	}

	@PostMapping("/delete_PRODUCTPLAN_list")
	@ResponseBody
	public ResponseEntity<Map<String, String>> deleteProductPlanList(
			@RequestBody List<Map<String, String>> requestList) {
		productplanService.deleteProductPlanList(requestList);

		// JSON 형태로 반환
		Map<String, String> response = new HashMap<>();
		response.put("status", "SUCCESS");
		return ResponseEntity.ok(response);
	}

	@GetMapping("/select_PROCESS_LIST")
	@ResponseBody
	public ResponseEntity<List<ProductPlanProcessDTO>> selectProcessList(
	        @RequestParam("contract_cd") String contractCd,
	        @RequestParam("product_cd") String productCd) {

	    List<ProductPlanProcessDTO> processList = productplanService.getProcessList(contractCd, productCd);
	    return ResponseEntity.ok(processList);
	}


}
