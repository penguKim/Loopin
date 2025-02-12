package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.PrimaryRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;
import com.itwillbs.c4d2412t3p1.domain.StockRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Stock;
import com.itwillbs.c4d2412t3p1.entity.Warearea;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.PrimaryService;
import com.itwillbs.c4d2412t3p1.service.StockService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.StockFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;



@RequiredArgsConstructor
@Controller
@Log
public class StockController {
	
	private final CommonService commonService;	
	private final StockService stockService;
	private final PrimaryService primaryService;
	
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
		System.out.println("---------------------필터----------------------------");
		log.info(filter.toString());
		Map<String, List<Common_codeDTO>> sizeList =  commonService.select_COMMON_list("SIZE");
		List<Map<String, Object>> list = stockService.select_STOCK_list(filter, sizeList);

		System.out.println(list.toString());
		
		response.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		response.put("data", data);
		
		return response;
	}
	
	// 재고 조회
	@ResponseBody
	@PostMapping("/select_STOCK")
	public Map<String, Object> select_STOCK(@RequestBody StockDTO stock) {
		Map<String, Object> response = new HashMap<>(); 
		
		// Map<String, List<Common_codeDTO>> sizeList =  commonService.select_COMMON_list("SIZE");
		// List<Map<String, Object>> list = stockService.select_STOCK_list(sizeList);

		List<Map<String, Object>> list = stockService.select_TEST();
		System.out.println("------------------리스트");
		System.out.println(list.toString());
		
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
	
	

}
