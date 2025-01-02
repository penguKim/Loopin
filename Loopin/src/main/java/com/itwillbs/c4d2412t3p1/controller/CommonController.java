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

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.Common_code_listDTO;
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
	@GetMapping("/select_common_code")
	public Map<String, Object> SELECT_COMMON_CODE(@RequestParam(name = "code", defaultValue = "") String code) {
	    List<Common_codeDTO> list = commonService.SELECT_COMMON_CODE(code);

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
	@PostMapping("/delete_group_code")
	public Map<String, Object> delete_group_code(@RequestBody List<Map<String, Object>> list) {
		log.info(list.toString());
		
		if(list.size() > 0) {
			commonService.delete_group_code(list);
		}
		
//	    List<Common_codeDTO> list = commonService.SELECT_COMMON_CODE(code);

	    Map<String, Object> response = new HashMap<>();
	    response.put("result", true);

	    Map<String, Object> data = new HashMap<>();
//	    data.put("contents", list);

	    response.put("data", data);
	    
	    return response;
	}
	
	@ResponseBody
	@PostMapping("/insert_group_code")
	public Map<String, Object> update_group_code(@RequestBody Common_code_listDTO list) {
//		public Map<String, Object> update_group_code(@RequestBody List<Map<String, Object>> createdRows,
//				@RequestBody List<Map<String, Object>> updatedRows, @RequestParam(name = "code", defaultValue = "") String code) {
		log.info(list.toString());
		List<Common_codeDTO> createdRows = list.getCreatedRows();
		List<Common_codeDTO> updatedRows = list.getUpdatedRows();
		String code = list.getCode();
		
		// 신규 행
		if(createdRows.size() > 0) {
			commonService.save(createdRows, code);
		}
		
		// 수정 행
		if(updatedRows.size() > 0) {
			
		}
//		
//		
	    List<Common_codeDTO> codeList = commonService.SELECT_COMMON_CODE(code);

	    Map<String, Object> response = new HashMap<>();
	    response.put("result", true);
//
	    Map<String, Object> data = new HashMap<>();
	    data.put("contents", codeList);
//
	    response.put("data", data);
		
		return response;
	}

	
	@ResponseBody
	@PostMapping("/INSERT_COMMON_CODE")
	public String INSERT_COMMON_CODE(@RequestBody List<Map<String, Object>> COMMON_CODE_LIST) {
		log.info(COMMON_CODE_LIST.toString());
		
		
		
		return "";
	}
	

	
	
}
