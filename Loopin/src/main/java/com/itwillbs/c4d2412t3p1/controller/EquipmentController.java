package com.itwillbs.c4d2412t3p1.controller;


import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.service.CommonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class EquipmentController {

	private final CommonService commonService;
	
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/equipment_list")
	public String equipment_list(Model model) {
	    
		Map<String, List<Common_codeDTO>> commonList =  commonService.select_COMMON_list("USE", "USEYN", "PRDTYPE");
		
		model.addAttribute("commonList", commonList);
		
		return "/primary/equipment_list";
	}
	
}