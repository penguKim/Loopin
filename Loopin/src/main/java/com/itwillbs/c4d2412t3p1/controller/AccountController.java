package com.itwillbs.c4d2412t3p1.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.AccountDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.domain.NoticeDTO;
import com.itwillbs.c4d2412t3p1.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class AccountController {
	
	private final AccountService accountService;
	
	
    // 거래처 등록 페이지로 이동
	@GetMapping("/account_list")
	public String account_list(Model model) {
		
		// 거래처유형
		model.addAttribute("ACtype_list", accountService.selectCommonList("ACTYPE"));

		// 사용여부
		model.addAttribute("useyn_list", accountService.selectCommonList("USEYN"));
		
		// 업태
		model.addAttribute("BTtype_list", accountService.selectCommonList("BTTYPE"));

		// 업종
		model.addAttribute("BItype_list", accountService.selectCommonList("BITYPE"));

		return "/account/account_list";
	}
	
	// 거래처 조회
	@GetMapping("/select_ACCOUNT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_ACCOUNT() {
		
		
		try {
	    	List<Map<String, Object>> response = accountService.select_ACCOUNT_DETAIL();
	    	return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	    
	}
	
	@PostMapping("/insert_ACCOUNT")
	public ResponseEntity<Map<String, String>> insert_ACCOUNT(
	    @RequestBody AccountDTO accountDTO
	) {
	    Map<String, String> response = new HashMap<>();
	    
	    // 시큐리티 세션 값 가져오기
	    String employee_id = SecurityContextHolder.getContext().getAuthentication().getName(); 
	    
	    try {
	        accountDTO.setAccount_wr(employee_id);
	        accountDTO.setAccount_wd(new Timestamp(System.currentTimeMillis()));

	        accountService.insert_ACCOUNT(accountDTO);

	        response.put("message", "데이터가 성공적으로 저장되었습니다.");
	        return ResponseEntity.ok(response); // JSON 형식으로 반환
	    } catch (Exception e) {
	        log.severe("데이터 저장 실패: " + e.getMessage()); // 오류 로그 추가
	        response.put("message", "데이터 저장 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	
	
	
} // 컨트롤러 끝
