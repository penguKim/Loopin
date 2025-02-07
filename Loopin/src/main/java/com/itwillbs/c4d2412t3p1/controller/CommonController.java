package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.Common_code_listDTO;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.UtilService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class CommonController {
	
	private final CommonService commonService;
	private final UtilService util;

	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN')")
	@GetMapping("common_code")
	public String common_code() {
		
		return "common/common_code";
	}
	
	@ResponseBody
	@PostMapping("/select_common_code")
	public ResponseEntity<Map<String, Object>> SELECT_COMMON_CODE(@RequestBody Common_code_listDTO commonDTO) {
		String code = util.checkNull(commonDTO.getCode());
		String filter = util.checkNull(commonDTO.getFilter());
		Map<String, Object> response = new HashMap<>();
		
		System.out.println("code: " + code);
		System.out.println("filter : " + filter);
	    try {
			List<Common_codeDTO> list = commonService.select_common_code(code, filter);
			response.put("result", true);
			Map<String, Object> data = new HashMap<>();
			data.put("contents", list);
			response.put("data", data);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.put("msg", "조회에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	

	@ResponseBody
	@PostMapping("/insert_common_code")
	public Map<String, Object> update_group_code(@RequestBody Common_code_listDTO commonDTO) {
		List<Common_codeDTO> createdRows = commonDTO.getCreatedRows();
		List<Common_codeDTO> updatedRows = commonDTO.getUpdatedRows();
		String code = util.checkNull(commonDTO.getCode());
		String filter = util.checkNull(commonDTO.getFilter());
		
	    Map<String, Object> result = commonService.insert_common_code(createdRows, updatedRows, code);

	    Map<String, Object> response = new HashMap<>();
	    
	    response.put("result", (int) result.get("insertCount") +  (int) result.get("updateCount")> 0);
		List<Common_codeDTO> codeList = commonService.select_common_code(code, filter);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", codeList);
		response.put("data", data);
		
		return response;
	}

	@ResponseBody
	@PostMapping("/delete_common_code")
	public Map<String, Object> delete_group_code(@RequestBody Common_code_listDTO commonDTO) {
		List<Common_codeDTO> deletedRows = commonDTO.getDeletedRows();
		String code = commonDTO.getCode();
		String filter = util.checkNull(commonDTO.getFilter());
		int deleteCount = 0;
		
		if(deletedRows.size() > 0) {
	        deleteCount = commonService.delete_common_code(deletedRows, code);
		}
		
		Map<String, Object> response = new HashMap<>();
		
	    response.put("result", deleteCount >= 0);
		List<Common_codeDTO> codeList = commonService.select_common_code(code, filter);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", codeList);
		response.put("data", data);
		
		return response;
	}

	
	@ResponseBody
	@PostMapping("/select_COMMON_list")
	public ResponseEntity<Map<String, Object>> select_COMMON_list(@RequestBody Common_code_listDTO commonDTO) {
		System.out.println("코드들은 : " + commonDTO.getList());
		Map<String, Object> response = new HashMap<>(); 
		Map<String, List<Common_codeDTO>> commonList =  commonService.select_COMMON_list(commonDTO.getList());
		log.info(commonList.toString());

		if(commonList.size() > 0) {
			response.put("commonList", commonList);
			
			return ResponseEntity.ok(response);
		} else {
			response.put("msg", "조회에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}

	
	
}