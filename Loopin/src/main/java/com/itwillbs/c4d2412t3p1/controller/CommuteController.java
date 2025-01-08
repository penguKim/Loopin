package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.Workinghour;
import com.itwillbs.c4d2412t3p1.repository.WorkinghourRepository;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.CommuteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class CommuteController {
	
	private final CommuteService commuteService;
	private final CommonService commonService;

	@GetMapping("/commute")
	public String commute() {
		return "/commute/commute_list";
	}
	
	@ResponseBody
	@GetMapping("/select_COMMUTE")
	public ResponseEntity<Map<String, Object>> select_COMMUTE(@RequestParam(name = "commute_dt", defaultValue = "") String commute_dt) {
		
		System.out.println("---------------------------------------- 파라미터 : " + commute_dt);
		
		List<Commute> list = commuteService.select_COMMUTE(commute_dt);
		
		
		Map<String, Object> response = new HashMap<>(); 
	    response.put("result", true);
	    Map<String, Object> data = new HashMap<>();
	    data.put("contents", list);
	    response.put("data", data);
		
		
		return ResponseEntity.ok(response);
	}
	
	
	@ResponseBody
	@PostMapping("/select_COMMUTE_list")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_list() {
		
		List<CommuteDTO> list = commuteService.select_COMMUTE_list();
		log.info(list.toString());
		
		Map<String, Object> response = new HashMap<>(); 
		response.put("result", true);
		response.put("list", list);
		
		return ResponseEntity.ok(response);
	}
	
	
	@ResponseBody
	@PostMapping("/select_COMMON_list")
	public ResponseEntity<Map<String, Object>> select_COMMON_list(@RequestBody List<String> list) {
		
		Map<String, Object> response = new HashMap<>(); 
		Map<String, List<Common_codeDTO>> commonList =  commonService.select_COMMON_list(list);
		log.info(commonList.toString());

		if(commonList.size() > 0) {
			response.put("result", true);
			response.put("list", commonList);
			
			return ResponseEntity.ok(response);
		} else {
			response.put("result", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	@GetMapping("/commute_type")
	public String commute_type(Model model) {
		
		List<Common_codeDTO> weekList = commuteService.select_COMMON_list("WEEK");
		model.addAttribute("WEEK", weekList);
		
		return "commute/commute_type";
	}
	
	@ResponseBody
	@GetMapping("/select_WORKINGHOUR")
	public ResponseEntity<Map<String, Object>> getMethodName() {
		
		List<Workinghour> list = commuteService.select_WORKINGHOUR();
		log.info(list.toString());
		
		Map<String, Object> response = new HashMap<>(); 
	    response.put("result", true);
	    Map<String, Object> data = new HashMap<>();
	    data.put("contents", list);
	    response.put("data", data);
	    
		return ResponseEntity.ok(response);
	}
	
	@ResponseBody
	@PostMapping("/select_WORKINGHOUR_detail")
	public ResponseEntity<Map<String, Object>> select_WORKINGHOUR_detail(@RequestParam("workinghour_id") String workinghour_id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Workinghour workinghour = commuteService.select_WORKINGHOUR_detail(workinghour_id);
			response.put("result", true);
			response.put("workinghour", workinghour);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	@ResponseBody
	@PostMapping("/insert_WORKINGHOUR")
	public ResponseEntity<Map<String, Object>> insert_WORKINGHOUR(@RequestBody WorkinghourDTO workinghourDTO) {
		
		Map<String, Object> response = new HashMap<>();
		try {
			Workinghour workinghour = commuteService.insert_WORKINGHOUR(workinghourDTO);
			
			List<Workinghour> list = commuteService.select_WORKINGHOUR();
			response.put("result", true);
			response.put("list", list);
			response.put("workinghour", workinghour);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	
	
}
