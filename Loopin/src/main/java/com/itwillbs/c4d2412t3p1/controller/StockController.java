package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.MaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.PrimaryRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;
import com.itwillbs.c4d2412t3p1.domain.StockRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Stock;
import com.itwillbs.c4d2412t3p1.entity.Warearea;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.PrimaryService;
import com.itwillbs.c4d2412t3p1.service.StockService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.StockFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;



@RequiredArgsConstructor
@Controller
@Log
public class StockController {
	
	private final CommonService commonService;	
	private final StockService stockService;
	private final PrimaryService primaryService;
	
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/stock_list")
	public String stock_list(Model model) {
		
		Map<String, List<Common_codeDTO>> commonList =  commonService.select_COMMON_list("SIZE");
		
		model.addAttribute("commonList", commonList);
		List<Map<String, String>> warehouseList = primaryService.select_WAREHOUSE_code();
		
		model.addAttribute("warehouseList", warehouseList);
		
		return "/stock/stock_list";
	}
	
	// 재고 조회
	@ResponseBody
	@PostMapping("/select_STOCK_list")
	public Map<String, Object> select_STOCK_list(@RequestBody StockRequestDTO stockRequest) {
		Map<String, Object> response = new HashMap<>(); 
		StockFilterRequest filter = stockRequest.getStockFilter();
		
		Map<String, List<Common_codeDTO>> sizeList =  commonService.select_COMMON_list("SIZE");
		List<Map<String, Object>> list = stockService.select_STOCK_list(filter, sizeList);

		response.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		response.put("data", data);
		
		return response;
	}
	
	// 품목 조회	
	@ResponseBody
	@PostMapping("/select_STOCK_{prefix}")
	public ResponseEntity<List<StockDTO>> select_STOCK(@PathVariable("prefix") String prefix,@RequestBody StockRequestDTO stockRequest) {
		
		StockFilterRequest filter = stockRequest.getStockFilter();
		List<StockDTO> list = null;
	
		if(prefix.equalsIgnoreCase("MATERIAL")) {
		    list = stockService.select_STOCK_MATERIAL(stockRequest.getMaterial_gc(), stockRequest.getMaterial_cc(), filter);
		} else if(prefix.equalsIgnoreCase("PRODUCT")) {
		    list = stockService.select_STOCK_PRODUCT(stockRequest.getProduct_gc(), stockRequest.getProduct_cc(), filter);
		}
		
		return ResponseEntity.ok(list);
	}
	
	// 재고 체크
	@ResponseBody
	@PostMapping("/check_STOCK_AQ")
	public ResponseEntity<Map<String, Object>> check_STOCK_AQ(@RequestBody StockRequestDTO stockRequest) {
		Map<String , Object> response = new HashMap<>();
		
		try {
			StockDTO stock = stockService.check_STOCK_AQ(stockRequest.getStock());
			response.put("stock", stock);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 재고 등록
	@LogActivity(value = "등록", action = "재고등록")
	@ResponseBody
	@PostMapping("/insert_STOCK")
	public ResponseEntity<Map<String, Object>> insert_STOCK(@RequestBody StockRequestDTO stockRequest) {
		Map<String , Object> response = new HashMap<>();
		StockFilterRequest filter = stockRequest.getStockFilter();
		
	    try {
	    	stockService.insert_STOCK(stockRequest.getStock());
	    	
			Map<String, List<Common_codeDTO>> sizeList =  commonService.select_COMMON_list("SIZE");
			List<Map<String, Object>> list = stockService.select_STOCK_list(filter, sizeList);
			response.put("list", list);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 차트 조회
	@ResponseBody
	@PostMapping("/select_STOCK_chart")
	public Map<String, Object> select_STOCK_chart(@RequestBody StockRequestDTO stockRequest) {
		Map<String, Object> response = new HashMap<>(); 
		StockFilterRequest filter = stockRequest.getStockFilter();

		List<Map<String, Object>> list = stockService.select_STOCK_chart(filter);
		
		response.put("list", list);
		
		return response;
	}
	
	// 창고구역 조회
	@ResponseBody
	@PostMapping("/select_WAREAREA_CC")
	public Map<String, Object> select_WAREAREA_CC(@RequestBody StockDTO stock) {
		Map<String, Object> response = new HashMap<>(); 
		
		List<Warearea> wareareaList = primaryService.select_WAREAREA_list(stock.getWarehouse_cd());
		
	    List<Map<String, Object>> extractedList = wareareaList.stream()
	        .map(ware -> {
	            Map<String, Object> map = new HashMap<>();
	            map.put("common_cc", ware.getWarearea_cd());
	            map.put("common_nm", ware.getWarearea_nm());
	            return map;
	        })
	        .collect(Collectors.toList());

	    response.put("list", extractedList);
		
		return response;
	}
	
	// 재고 삭제
	@LogActivity(value = "삭제", action = "재고삭제")
	@ResponseBody
	@PostMapping("/delete_STOCK")
	public ResponseEntity<Map<String, Object>> delete_STOCK(@RequestBody StockRequestDTO stockRequest) {
		List<Map<String, Object>> stockList = stockRequest.getStockList();
		Map<String, Object> response = new HashMap<>();
		
		try {
			stockService.delete_STOCK(stockList);
			
			Map<String, List<Common_codeDTO>> sizeList =  commonService.select_COMMON_list("SIZE");
			List<Map<String, Object>> list = stockService.select_STOCK_list(new StockFilterRequest(), sizeList);
			response.put("list", list);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "창고 삭제 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}

}
