package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.InoutDTO;
import com.itwillbs.c4d2412t3p1.domain.InoutRequestDTO;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.InoutService;
import com.itwillbs.c4d2412t3p1.service.PrimaryService;


@RequiredArgsConstructor
@Controller
@Log
public class InoutController {
	
	private final CommonService commonService;	
	private final PrimaryService primaryService;
	private final InoutService inoutService;
	
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/inout_list")
	public String inout_list(Model model) {
		
		Map<String, List<Common_codeDTO>> commonList =  commonService.select_COMMON_list("SIZE");
		
		model.addAttribute("commonList", commonList);
		List<Map<String, String>> warehouseList = primaryService.select_WAREHOUSE_code();
		
		model.addAttribute("warehouseList", warehouseList);
		
		return "/inout/inout_list";
	}
	
	// 입출고 조회
	@ResponseBody
	@PostMapping("/select_INOUT_list")
	public Map<String, Object> select_INOUT_list(@RequestBody InoutRequestDTO inoutRequest) {
		Map<String, Object> response = new HashMap<>(); 
		List<InoutDTO> list = inoutService.select_INOUT_list();

		System.out.println(list.toString());
		
		response.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		response.put("data", data);
		
		return response;
	}
	


	

}
