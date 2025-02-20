package com.itwillbs.c4d2412t3p1.controller;


import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.EquipmentDTO;
import com.itwillbs.c4d2412t3p1.domain.EquipmentRequestDTO;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.LotService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.CommuteFilterRequest;
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
		log.info("params"+params);
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
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			log.info("params" + params);
			List<Map<String, Object>> results = lotService.select_RESULT_list(params);
			response.put("data", results);
			log.info("results" + results);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
//	// 생산실적 차트
//	@ResponseBody
//	@PostMapping("/select_RESULT_chart")
//	public ResponseEntity<Map<String, Object>> select_RESULT_chart(@RequestBody(required = false) Map<String, Object> params) {
//		Map<String, Object> response = new HashMap<>();
//		String sort = "";
//		
//		try {
//			
//			// 일자별 근로시간 차트 ------------------------------------------
//	        List<CommuteDTO> dayCommuteData = lotService.select_COMMUTE_dayCommuteChart(filter);
//	        Map<String, Object> dayCommuteChart = new HashMap<>();
//	        
//	        List<String> dayCommuteCategories = dayCommuteData.stream()
//	            .map(CommuteDTO::getCommute_wd)
//	            .collect(Collectors.toList());
//	            
//	        List<Map<String, Object>> dayCommuteSeries = Arrays.asList(
//        		lotService.createSeriesData("일반근무", CommuteDTO::getCommute_ig, dayCommuteData),
//        		lotService.createSeriesData("연장근무", CommuteDTO::getCommute_eg, dayCommuteData),
//        		lotService.createSeriesData("야간근무", CommuteDTO::getCommute_yg, dayCommuteData),
//        		lotService.createSeriesData("주말근무", CommuteDTO::getCommute_jg, dayCommuteData),
//        		lotService.createSeriesData("휴일근무", CommuteDTO::getCommute_hg, dayCommuteData)
//	        );
//	        
//	        dayCommuteChart.put("categories", dayCommuteCategories);
//	        dayCommuteChart.put("series", dayCommuteSeries);
//	        response.put("dayCommuteChart", dayCommuteChart);
//	        
//	        // 근로시간 차트 ------------------------------------------	
//	        List<CommuteDTO> commuteData = lotService.select_COMMUTE_commuteChart(filter);
//	        Map<String, Object> commuteChart = new HashMap<>();
//	        List<String> commuteCategories = Arrays.asList("근무시간");		            
//	        List<Map<String, Object>> commuteSeries = Arrays.asList(
//        		lotService.createSeriesData("일반근무", CommuteDTO::getCommute_ig, commuteData),
//        		lotService.createSeriesData("연장근무", CommuteDTO::getCommute_eg, commuteData),
//        		lotService.createSeriesData("야간근무", CommuteDTO::getCommute_yg, commuteData),
//        		lotService.createSeriesData("주말근무", CommuteDTO::getCommute_jg, commuteData),
//        		lotService.createSeriesData("휴일근무", CommuteDTO::getCommute_hg, commuteData)
//	        );
//	        
//	        commuteChart.put("categories", commuteCategories);
//	        commuteChart.put("series", commuteSeries);
//	        response.put("commuteChart", commuteChart);
//	        
//	        // 직급별 바 차트 ------------------------------------------
//	        sort = "POSITION";
//	        List<CommuteDTO> gradeData = lotService.select_COMMUTE_barChart(sort, filter);
//	        Map<String, Object> gradeChart = new HashMap<>();
//	        
//	        List<String> gradeCategories = gradeData.stream()
//	            .map(CommuteDTO::getEmployee_gd)
//	            .collect(Collectors.toList());
//	            
//	        List<Map<String, Object>> gradeSeries = Arrays.asList(
//        		lotService.createSeriesData("정상출근", CommuteDTO::getNormal, gradeData),
//        		lotService.createSeriesData("지각", CommuteDTO::getLate, gradeData)
//	        );
//	        
//	        gradeChart.put("categories", gradeCategories);
//	        gradeChart.put("series", gradeSeries);
//	        response.put("gradeChart", gradeChart);
//	        
//	        // 부서별 바 차트 ------------------------------------------
//	        sort = "DEPARTMENT";
//	        List<CommuteDTO> deptData = commuteService.select_COMMUTE_barChart(sort, filter);
//	        Map<String, Object> deptChart = new HashMap<>();
//	        System.out.println("DEPARTMENT : " + deptData.toString());
//	        List<String> deptCategories = deptData.stream()
//	            .map(CommuteDTO::getEmployee_gd)
//	            .collect(Collectors.toList());
//	            
//	        List<Map<String, Object>> deptSeries = Arrays.asList(
//	        		commuteService.createSeriesData("정상출근", CommuteDTO::getNormal, deptData),
//	        		commuteService.createSeriesData("지각", CommuteDTO::getLate, deptData)
//	        );
//	        
//	        deptChart.put("categories", deptCategories);
//	        deptChart.put("series", deptSeries);
//	        response.put("deptChart", deptChart);
//	        
//			return ResponseEntity.ok(response);
//		} catch (Exception e) {
//			response.put("msg", e.getMessage());
//			
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//		}
//	}
	
	
	
}