package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.service.CommuteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class CommuteController {
	
	private final CommuteService commuteService;

	@GetMapping("/commute")
	public String commute() {
		return "/commute/commute_list";
	}
	
	@ResponseBody
	@GetMapping("/select_COMMUTE")
	public ResponseEntity<Map<String, Object>> select_COMMUTE(@RequestParam(name = "commute_dt") String commute_dt) {
		
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
	public ResponseEntity<Map<String, Object>> getMethodName() {
		
		List<CommuteDTO> list = commuteService.select_COMMUTE_list();
		log.info(list.toString());
		
		Map<String, Object> response = new HashMap<>(); 
		response.put("result", true);
		response.put("list", list);
		
		return ResponseEntity.ok(response);
	}
	
	
}
