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

import com.itwillbs.c4d2412t3p1.domain.ContractDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.ContractRequestDTO;
import com.itwillbs.c4d2412t3p1.service.BusinessService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class BusinessController {

	private final BusinessService businessService ;
	
	
	// 수주관리 페이지로 이동
	@GetMapping("/contract_list")
	public String contract_list(Model model) {
		
		// 수주상태
		model.addAttribute("status_list", businessService.selectCommonList("STATUS"));
		
		return "/business/contract_list";
	}
	
	// 제품 조회
	@GetMapping("/select_RPODUCT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_RPODUCT() {
		
		try {
	    	List<Map<String, Object>> response = businessService.select_RPODUCT();
	    	
	    	return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	    
	}

	// 수주 조회
	@GetMapping("/select_CONTRACT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_CONTRACT() {
		
		try {
			List<Map<String, Object>> response = businessService.select_CONTRACT();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
		
	}

	// 수주상세 조회
	@GetMapping("/select_CONTRACTDETAIL")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_CONTRACTDETAIL(
			@RequestParam(name = "contract_cd") String contractCd) {
		
		try {
			List<Map<String, Object>> details = businessService.select_CONTRACTDETAIL(contractCd);
			
			return ResponseEntity.ok(details);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	// 거래처 조회
	@GetMapping("/select_ACCOUNT_CONTRACT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_ACCOUNT_CONTRACT() {
		
		try {
			List<Map<String, Object>> response = businessService.select_ACCOUNT_CONTRACT();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
		
	}

	// 담당자 조회
	@GetMapping("/select_CONTRACT_PS")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_CONTRACT_PS() {
		
		try {
			List<Map<String, Object>> response = businessService.select_CONTRACT_PS();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
		
	}
	
	@ResponseBody
	@PostMapping("/insert_CONTRACT")
	public ResponseEntity<Map<String, String>> insert_CONTRACT(
			@RequestBody ContractRequestDTO contractRequestDTO
	) {
	    Map<String, String> response = new HashMap<>();
	    
	    
	    // 시큐리티 세션 값 가져오기
	    String employee_id = SecurityContextHolder.getContext().getAuthentication().getName(); 
	    
	    try {
	    	
	    	ContractDTO contractDto = contractRequestDTO.getContract();
	    	contractDto.setContract_wr(employee_id);
	    	contractDto.setContract_wd(new Timestamp(System.currentTimeMillis()));
	    	
	    	List<ContractDetailDTO> details = contractRequestDTO.getDetails();

	        businessService.insert_CONTRACT(contractDto, details);

	        response.put("message", "데이터가 성공적으로 저장되었습니다.");
	        return ResponseEntity.ok(response); // JSON 형식으로 반환
	    } catch (Exception e) {
	        log.severe("데이터 저장 실패: " + e.getMessage()); // 오류 로그 추가
	        response.put("message", "데이터 저장 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	@GetMapping("/get_CONTRACT")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getContractDetails(@RequestParam(name = "contract_cd") String contractCd) {
	    try {
	        // 서비스에서 계약 정보 + 상세 정보 조회
	        Map<String, Object> contractData = businessService.getContractDetails(contractCd);

	        if (contractData.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(Map.of("error", "계약 데이터를 찾을 수 없습니다."));
	        }

	        return ResponseEntity.ok(contractData);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "데이터 조회 중 오류 발생"));
	    }
	}

	@ResponseBody
	@PostMapping("/update_CONTRACT")
	public ResponseEntity<Map<String, String>> update_CONTRACT(
	        @RequestBody ContractRequestDTO contractRequestDTO
	) {
	    Map<String, String> response = new HashMap<>();

	    // 시큐리티 세션 값 가져오기
	    String employee_id = SecurityContextHolder.getContext().getAuthentication().getName();

	    try {
	        ContractDTO contractDto = contractRequestDTO.getContract();
	        
	        contractDto.setContract_mf(employee_id); // 수정자 설정
	        contractDto.setContract_md(new Timestamp(System.currentTimeMillis())); // 수정 시간 설정

	        List<ContractDetailDTO> details = contractRequestDTO.getDetails();

	        // 수정 서비스 호출
	        businessService.update_CONTRACT(contractDto, details);

	        response.put("message", "수정이 성공적으로 완료되었습니다.");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        log.severe("수정 실패: " + e.getMessage());
	        response.put("message", "수정 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}


	
	
}
