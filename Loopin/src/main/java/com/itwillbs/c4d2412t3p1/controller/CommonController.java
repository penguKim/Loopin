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
	    List<Common_codeDTO> list = commonService.select_common_code(code);

	    Map<String, Object> response = new HashMap<>();
	    response.put("result", true);

	    Map<String, Object> data = new HashMap<>();
	    data.put("contents", list);

	    response.put("data", data);
	    
	    return response;
	}
	

	@ResponseBody
	@PostMapping("/insert_common_code")
	public Map<String, Object> update_group_code(@RequestBody Common_code_listDTO list) {
		log.info(list.toString());
		List<Common_codeDTO> createdRows = list.getCreatedRows();
		List<Common_codeDTO> updatedRows = list.getUpdatedRows();
		String code = list.getCode();
		
	    Map<String, Object> result = commonService.insert_common_code(createdRows, updatedRows, code);

	    Map<String, Object> response = new HashMap<>();
	    System.out.println("-------------- 인서트 갯수 : " + result.get("insertCount"));
	    System.out.println("-------------- 업데이트 갯수 : " + result.get("updateCount"));
	    
	    response.put("result", (int) result.get("insertCount") +  (int) result.get("updateCount")> 0);
		List<Common_codeDTO> codeList = commonService.select_common_code(code);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", codeList);
		response.put("data", data);
		
		return response;
	}

	@ResponseBody
	@PostMapping("/delete_common_code")
	public Map<String, Object> delete_group_code(@RequestBody Common_code_listDTO list) {
		log.info(list.toString());
		List<Common_codeDTO> deletedRows = list.getDeletedRows();
		String code = list.getCode();
		int deleteCount = 0;
		
		if(deletedRows.size() > 0) {
	        deleteCount = commonService.delete_common_code(deletedRows, code);
		}
		
		Map<String, Object> response = new HashMap<>();
		
		if(deleteCount <= 0) {
			response.put("result", false);			
		} else {
			List<Common_codeDTO> codeList = commonService.select_common_code(code);
			response.put("result", true);			
			Map<String, Object> data = new HashMap<>();
			data.put("contents", codeList);
			response.put("data", data);
		}
		
	    response.put("result", deleteCount <= 0);
		List<Common_codeDTO> codeList = commonService.select_common_code(code);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", codeList);
		response.put("data", data);
		
		return response;
	}

	

	
	
}
