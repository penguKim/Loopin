package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.service.CommonService;



@RequiredArgsConstructor
@Controller
@Log
public class StockController {
	
	private final CommonService commonService;	

	@GetMapping("/stock_list")
	public String stock_list(Model model) {
		
		Map<String, List<Common_codeDTO>> commonList =  commonService.select_COMMON_list("SIZE");
		
		model.addAttribute("commonList", commonList);
		
		return "/stock/stock_list";
	}
	

}
