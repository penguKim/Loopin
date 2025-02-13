package com.itwillbs.c4d2412t3p1.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.EquipmentDTO;
import com.itwillbs.c4d2412t3p1.domain.EquipmentRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.PrimaryRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.EquipmentService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.EquipmentFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class EquipmentController {

	private final CommonService commonService;
	private final EquipmentService equipmentService;
	
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_MF_ADMIN')")
	@GetMapping("/equipment_list")
	public String equipment_list(Model model) {
	    
		Map<String, List<Common_codeDTO>> commonList = commonService.select_COMMON_list("USE", "USEYN", "WHTYPE");
		List<Common_codeDTO> productList = equipmentService.select_PRODUCT_list();
		commonList.put("productList", productList);
		model.addAttribute("commonList", commonList);
		
		return "/primary/equipment_list";
	}
	
	@ResponseBody
	@PostMapping("/select_EQUIPMENT")
	public Map<String, Object> select_EQUIPMENT(@RequestBody EquipmentRequestDTO equipmentDTO) {

		EquipmentFilterRequest filter = new EquipmentFilterRequest();
		
		if(equipmentDTO.getEquipmentFilter() == null) {
			filter.setEquipment_cd(equipmentDTO.getEquipment_cd());
		} else {
			filter = equipmentDTO.getEquipmentFilter();
		}
		
		List<EquipmentDTO> equipmentList = equipmentService.select_EQUIPMENT(filter);
		
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> data = new HashMap<>();
		
		data.put("contents", equipmentList);
		response.put("data", data);
		log.info("contents"+ equipmentList);
		return response;
	}

	// 설비 등록
	@LogActivity(value = "등록", action = "설비등록")
	@ResponseBody
	@PostMapping("/insert_EQUIPMENT")
	public ResponseEntity<Map<String, Object>> insert_EQUIPMENT(@RequestPart(value = "requestData") EquipmentRequestDTO equipmentDTO,
		    @RequestPart(value = "image", required = false) MultipartFile image) {
		
		EquipmentDTO equipment = equipmentDTO.getEquipment();
		EquipmentFilterRequest filter = new EquipmentFilterRequest();
		Map<String, Object> response = new HashMap<>();

		try {
	    	equipmentService.insert_EQUIPMENT(equipment, image);
	    	
			List<EquipmentDTO> list = equipmentService.select_EQUIPMENT(filter);
			response.put("list", list);
	    	response.put("success", true);
			return ResponseEntity.ok(response);
		
		} catch (Exception e) {
			e.printStackTrace();
			response.put("FALSE", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 설비 삭제
	@LogActivity(value = "삭제", action = "설비삭제")
	@ResponseBody
	@PostMapping("/delete_EQUIPMENT")
	public ResponseEntity<Map<String, Object>> delete_EQUIPMENT(@RequestBody EquipmentRequestDTO equipmentDTO) {
		List<EquipmentDTO> equipmentList = equipmentDTO.getEquipmentList();
		EquipmentFilterRequest filter = equipmentDTO.getEquipmentFilter();
		Map<String, Object> response = new HashMap<>();
		
		try {
			equipmentService.delete_EQUIPMENT(equipmentList);
			
			List<EquipmentDTO> list = equipmentService.select_EQUIPMENT(filter);
			
			response.put("list", list);
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "삭제 중 오류가 발생했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
}