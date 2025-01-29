package com.itwillbs.c4d2412t3p1.controller;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Holiday;
import com.itwillbs.c4d2412t3p1.entity.Workinghour;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.CommuteService;
import com.itwillbs.c4d2412t3p1.service.EmployeeService;
import com.itwillbs.c4d2412t3p1.service.PrimaryService;
import com.itwillbs.c4d2412t3p1.service.UtilService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.CommuteFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import oracle.jdbc.proxy.annotation.Post;

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
	    
		return "/primary/warehouse_list";
	}
	
	@ResponseBody
	@PostMapping("/select_WAREHOUSE_list")
	public Map<String, Object> select_WAREHOUSE_list() {
		String filter = "";
		Map<String, Object> response = new HashMap<>(); 
		List<WarehouseDTO> list = primaryService.select_WAREHOUSE_list(filter);
		
		response.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		response.put("data", data);
		
		return response;
	}
	

	

	
}