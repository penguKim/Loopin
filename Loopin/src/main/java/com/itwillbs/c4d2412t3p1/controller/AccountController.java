package com.itwillbs.c4d2412t3p1.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.AccountDTO;
import com.itwillbs.c4d2412t3p1.entity.Account;
import com.itwillbs.c4d2412t3p1.service.AccountService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.AccountFilterRequest;

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

	@PostMapping("/update_ACCOUNT")
	public ResponseEntity<Map<String, String>> update_ACCOUNT(
			@RequestBody AccountDTO accountDTO
			) {
		Map<String, String> response = new HashMap<>();
		
		// 시큐리티 세션 값 가져오기
		String employee_id = SecurityContextHolder.getContext().getAuthentication().getName(); 
		
		try {
			
	    	String account_cd = accountDTO.getAccount_cd();
	    	
	        if (account_cd == null) {
	            response.put("message", "데이터 수정 실패: ID(account_cd)가 전달되지 않았습니다.");
	            return ResponseEntity.badRequest().body(response);
	        }
			
	        // 기존 데이터 조회
	        Account account = accountService.findNoticeById(accountDTO.getAccount_cd());
	        if (account == null) {
	            response.put("message", "데이터 수정 실패: 해당 CD의 데이터를 찾을 수 없습니다.");
	            return ResponseEntity.badRequest().body(response);
	        }
	        // 기존 account_wr 값 유지 
			accountDTO.setAccount_wr(account.getAccount_wr());
			
			// 기존 account_wd 값 유지 
			accountDTO.setAccount_wd(account.getAccount_md());
			
			// 수정자 처리
			accountDTO.setAccount_mf(employee_id);
			
			// 수정일 처리
			accountDTO.setAccount_md(new Timestamp(System.currentTimeMillis()));
			
			accountService.update_ACCOUNT(accountDTO);
			
			// 성공 응답
			response.put("message", "데이터가 성공적으로 수정되었습니다.");
			return ResponseEntity.ok(response); // JSON 형식으로 반환
			
	    } catch (IllegalArgumentException e) {
	        // ID로 조회되지 않는 경우 처리
	        response.put("message", "데이터 수정 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			
		} catch (Exception e) {
			log.severe("데이터 저장 실패: " + e.getMessage()); // 오류 로그 추가
			response.put("message", "데이터 수정 실패: 알 수 없는 오류가 발생했습니다. " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	//	거래처 삭제(거래처 사용여부 미사용 지정)
	@PostMapping("/delete_ACCOUNT")
	public ResponseEntity<Map<String, Object>> delete_ACCOUNT(@RequestBody Map<String, List<String>> request) {
		
		List<String> accountCds  = request.get("account_cds");
		
		log.info("삭제 요청 데이터: " + request.toString());
		
		Map<String, Object> response = new HashMap<>();

		try {
			accountService.updateAccountStatus(accountCds, "0");
			response.put("success", true);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
    // 필터 데이터 가져오기
	@PostMapping("/select_FILTERED_ACCOUNT")
    public ResponseEntity<List<Map<String, Object>>> select_FILTERED_ACCOUNT(@RequestBody AccountFilterRequest filterRequest) {

	    try {
	            // 필터 조건이 비어 있으면 전체 인사정보 반환
	            if (filterRequest.isEmpty()) {
	                List<Map<String, Object>> accounts = accountService.select_ACCOUNT_DETAIL();
	                log.info("accounts : "+ accounts);
	                return ResponseEntity.ok(accounts);
	            }

	            // 필터 조건에 따른 필터링된 인사정보 반환
	            List<Map<String, Object>> filteredAccountList = accountService.select_FILTERED_ACCOUNT(filterRequest);
	            log.info("filteredAccountList : "+ filteredAccountList);
	            return ResponseEntity.ok(filteredAccountList);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
    }
	
	// 담당자 조회
	@GetMapping("/select_ACCOUNT_PS")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_CONTRACT_PS() {
		
		try {
			List<Map<String, Object>> response = accountService.select_ACCOUNT_PS();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
		
	}
	
	// 담당자 검색
	@GetMapping("/search_ACCOUNT_PS")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> search_ACCOUNT_PS(@RequestParam("keyword") String keyword) {
	    try {
	    	List<Map<String, Object>> result;
	    	
	        if (keyword == null || keyword.trim().isEmpty()) {
	            // 검색어가 없으면 전체 조회 실행
	            result = accountService.select_ACCOUNT_PS();
	        } else {
	            // 검색어가 있으면 필터링 실행
	            result = accountService.search_ACCOUNT_PS(keyword);
	        }
	    	
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}
	
} // 컨트롤러 끝
