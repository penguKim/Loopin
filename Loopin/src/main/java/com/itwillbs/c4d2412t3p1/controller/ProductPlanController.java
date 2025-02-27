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
import com.itwillbs.c4d2412t3p1.domain.ContractDetailProductInfoDTO;
import com.itwillbs.c4d2412t3p1.domain.DailyProductPlanDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeListDTO;
import com.itwillbs.c4d2412t3p1.domain.LotResponse;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanProcessDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanSaveRequest;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanSaveRequest.ProcessOrderDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseListDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkableEmployeeProjection;
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
	public ResponseEntity<List<ProductPlanProcessDTO>> selectProcessList(@RequestParam("contract_cd") String contractCd,
			@RequestParam("product_cd") String productCd) {

		List<ProductPlanProcessDTO> processList = productplanService.getProcessList(contractCd, productCd);
		return ResponseEntity.ok(processList);
	}

	/**
	 * (수주번호, 품목코드)에 해당하는 Color/Size/수량 리스트 반환
	 */
	@GetMapping("/select_CONTRACTDETAIL_colorsizes")
	@ResponseBody // JSON 응답
	public ResponseEntity<List<ContractDetailProductInfoDTO>> selectContractDetailColorSizes(
			@RequestParam("contract_cd") String contractCd, @RequestParam("product_cd") String productCd) {

		// 1) Service 호출 -> DTO 목록
		List<ContractDetailProductInfoDTO> list = productplanService.findColorSizeList(contractCd, productCd);
		log.info(list.toString());

		// 2) ResponseEntity로 반환
		return ResponseEntity.ok(list);
	}

//	/**
//	 * 일일생산계획 모달에서 "작업자" 검색 시 → 근무 가능 사원 목록 ex)
//	 */
//	@GetMapping("/select_WORKABLE_EMPLOYEE_list")
//	@ResponseBody
//	public ResponseEntity<List<WorkableEmployeeProjection>> select_WORKABLE_EMPLOYEE_list(
//			@RequestParam("workDate") String workDate, @RequestParam("productCd") String productCd,
//			@RequestParam("processCd") String processCd) {
//		List<WorkableEmployeeProjection> list = productplanService.select_WORKABLE_EMPLOYEE_list(workDate, productCd,
//				processCd);
//		return ResponseEntity.ok(list);
//	}

	@PostMapping("/save_DAILYPRODUCTPLAN")
	public ResponseEntity<Map<String, String>> saveDailyPlan(@RequestBody DailyProductPlanDTO dto) {
		productplanService.saveDailyPlan(dto);

		Map<String, String> response = new HashMap<>();
		response.put("status", "SUCCESS");
		return ResponseEntity.ok(response);
	}

	// 2) 조회
	@GetMapping("/select_DAILYPRODUCTPLAN_list")
	public ResponseEntity<List<DailyProductPlanDTO>> selectDailyPlanList(@RequestParam("contract_cd") String contractCd,
			@RequestParam("base_product_cd") String baseProductCd) {
		List<DailyProductPlanDTO> list = productplanService.findDailyPlanList(contractCd, baseProductCd);
		log.info("###" + list.toString());
		return ResponseEntity.ok(list);
	}

	// 작업지시

	@GetMapping("/select_LOT_list_for_workorder")
	public ResponseEntity<List<LotResponse>> selectLotList() {
		// Service -> DTO List
		List<LotResponse> list = productplanService.getLotList();

		return ResponseEntity.ok(list);
	}

	// 작업자 조회
	@GetMapping("/select_WORKABLE_EMPLOYEE_list")
	public ResponseEntity<List<WorkableEmployeeProjection>> selectWorkableEmployeeList(
			@RequestParam("workDate") String workDate, @RequestParam("productCd") String productCd,
			@RequestParam("processCd") String processCd) {
		log.info("사원정보~~ " + workDate + productCd + processCd);
		List<WorkableEmployeeProjection> list = productplanService.select_WORKABLE_EMPLOYEE_list(workDate, productCd,
				processCd);

		return ResponseEntity.ok(list);
	}
	
	// 
    @GetMapping("/select_DAILYPLAN_one")
    public ResponseEntity<Map<String, String>> selectDailyPlanOne(
        @RequestParam("baseProductCd") String baseProductCd,
        @RequestParam("contractCd")    String contractCd,
        @RequestParam("processCd")     String processCd,
        @RequestParam("productCr")     String productCr,
        @RequestParam("productSz")     String productSz
    ) {
        // 1) Service 호출
        String dailyDate = productplanService.findDailyPlanDate(
            baseProductCd, contractCd, processCd, productCr, productSz
        );

        // 2) JSON 응답: {"dailyproductplan_sd":"YYYY-MM-DD"}
        Map<String, String> result = new HashMap<>();
        result.put("dailyproductplan_sd", (dailyDate != null) ? dailyDate : "");

        return ResponseEntity.ok(result);
    }

}
