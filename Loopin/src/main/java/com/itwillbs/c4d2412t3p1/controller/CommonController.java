package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.service.CommonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class CommonController {
	
	private final CommonService commonService;

	@GetMapping("common_code")
	public String common_code() {
		
		
		
		return "common/common_code";
	}
	
	@ResponseBody
	@GetMapping("test222")
	public Map<String, Object> SELECT_COMMON_CODE(@RequestParam(name = "code", defaultValue = "") String code) {
		log.info("--------------------------------test-----------------------------------");
		
		log.info("파라미터로 넘어온건 무엇이" + code);
		List<Map<String, Object>> list =  commonService.SELECT_COMMON_CODE(code);
//		
	    // Toast Grid에 맞는 응답 형식으로 변환
	    Map<String, Object> response = new HashMap<>();
	    response.put("result", true);
	    Map<String, Object> data = new HashMap<>();
	    data.put("contents", list);
	    response.put("data", data);

	    return response;
	}
	
	@PostMapping("/test5")
	public String modifyData(@RequestBody Map<String, List<Map<String, Object>>> changes) {
	    System.out.println("복합 작업 데이터:" + changes);
	    return "";
	}

	

	
	@ResponseBody
	@PostMapping("/INSERT_COMMON_CODE")
	public String INSERT_COMMON_CODE(@RequestBody List<Map<String, Object>> COMMON_CODE_LIST) {
		log.info(COMMON_CODE_LIST.toString());
		
		
		
		return "";
	}
	

	
	
}
