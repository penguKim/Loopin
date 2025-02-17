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
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ContractFilterRequest;

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

	// 제품 검색 기능 추가 (제품명, 제품코드, 색상, 사이즈, 기준단위)
	@GetMapping("/search_PRODUCT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> search_PRODUCT(@RequestParam("keyword") String keyword) {
	    try {
	    	List<Map<String, Object>> result;
	    	
	        if (keyword == null || keyword.trim().isEmpty()) {
	            // 검색어가 없으면 전체 조회 실행
	            result = businessService.select_RPODUCT();
	        } else {
	            // 검색어가 있으면 필터링 실행
	            result = businessService.search_PRODUCT(keyword);
	        }
	        
	        
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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

	// 거래처 검색
	@GetMapping("/search_ACCOUNT_CONTRACT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> search_ACCOUNT_CONTRACT(@RequestParam("keyword") String keyword) {
	    try {
	    	
	    	List<Map<String, Object>> result;
	    	
	        if (keyword == null || keyword.trim().isEmpty()) {
	            // 검색어가 없으면 전체 조회 실행
	            result = businessService.select_ACCOUNT_CONTRACT();
	        } else {
	            // 검색어가 있으면 필터링 실행
	            result = businessService.search_ACCOUNT_CONTRACT(keyword);
	        }

	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
	
	// 담당자 검색
	@GetMapping("/search_CONTRACT_PS")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> search_CONTRACT_PS(@RequestParam("keyword") String keyword) {
	    try {
	    	List<Map<String, Object>> result;
	    	
	        if (keyword == null || keyword.trim().isEmpty()) {
	            // 검색어가 없으면 전체 조회 실행
	            result = businessService.select_CONTRACT_PS();
	        } else {
	            // 검색어가 있으면 필터링 실행
	            result = businessService.search_CONTRACT_PS(keyword);
	        }
	    	
	        return ResponseEntity.ok(result);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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
	                    .body(Map.of("error", "수주 데이터를 찾을 수 없습니다."));
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

	//	수주 삭제
	@PostMapping("/delete_CONTRACT")
	public ResponseEntity<Map<String, Object>> delete_CONTRACT(@RequestBody Map<String, List<String>> request) {
		
		List<String> contractCds  = request.get("contract_cds");
		
		log.info("삭제 요청 데이터: " + request.toString());
		
		Map<String, Object> response = new HashMap<>();

		try {
            businessService.delete_ContractAndDetails(contractCds);
            response.put("success", true);
            return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
    // 필터 데이터 가져오기
	@PostMapping("/select_FILTERED_CONTRACT")
    public ResponseEntity<List<Map<String, Object>>> select_FILTERED_CONTRACT(@RequestBody ContractFilterRequest filterRequest) {

	    try {
            // 필터 조건이 비어 있으면 전체 수주정보 반환
            if (filterRequest.isEmpty()) {
                List<Map<String, Object>> contracts = businessService.select_CONTRACT();
                log.info("contracts : "+ contracts);
                return ResponseEntity.ok(contracts);
            }

            // 필터 조건에 따른 필터링된 수주정보 반환
            List<Map<String, Object>> filteredContractList = businessService.select_FILTERED_CONTRACT(filterRequest);
            log.info("filteredContractList : "+ filteredContractList);
            return ResponseEntity.ok(filteredContractList);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
    }
	
    // 수동으로 상태 업데이트 실행
    @PostMapping("/updateContractStatus")
    public String updateContractStatus() {
        businessService.updateContractStatus(); // 상태 업데이트 실행
        return "redirect:/contract_list"; // 상태 업데이트 후 목록 페이지로 이동
    }
    
    

	// 출하관리 페이지로 이동
	@GetMapping("/shipment_list")
	public String shipment_list(Model model) {
		
		return "/business/shipment_list";
	}
    
    
    // 상세 페이지
	@GetMapping("/get_CONTRACT_SHIPMENT")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> get_CONTRACT_SHIPMENT(@RequestParam(name = "contract_cd") String contractCd) {
	    try {
	        // 서비스에서 계약 정보 + 상세 정보 조회
	        Map<String, Object> contractData = businessService.get_CONTRACT_SHIPMENT(contractCd);

	        if (contractData.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(Map.of("error", "출하 데이터를 찾을 수 없습니다."));
	        }

	        return ResponseEntity.ok(contractData);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "데이터 조회 중 오류 발생"));
	    }
	}
	
	// 출하페이지 수주 조회
	@GetMapping("/select_CONTRACT_SHIPMENT")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_CONTRACT_SHIPMENT() {
		
		try {
			List<Map<String, Object>> response = businessService.select_CONTRACT_SHIPMENT();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
		
	}
	
    // 필터 데이터 가져오기
	@PostMapping("/select_FILTERED_CONTRACT_SHIPMENT")
    public ResponseEntity<List<Map<String, Object>>> select_FILTERED_CONTRACT_SHIPMENT(@RequestBody ContractFilterRequest filterRequest) {

	    try {
            // 필터 조건이 비어 있으면 전체 수주정보 반환
            if (filterRequest.isEmpty()) {
                List<Map<String, Object>> contracts = businessService.select_CONTRACT_SHIPMENT();
                log.info("contracts : "+ contracts);
                return ResponseEntity.ok(contracts);
            }

            // 필터 조건에 따른 필터링된 수주정보 반환
            List<Map<String, Object>> filteredContractList = businessService.select_FILTERED_CONTRACT_SHIPMENT(filterRequest);
            log.info("filteredContractList : "+ filteredContractList);
            return ResponseEntity.ok(filteredContractList);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
    }
	
	
	@PostMapping("/update_CONTRACT_SHIPMENT")
	@ResponseBody
	public Map<String, Object> updateContractShipment(@RequestBody Map<String, Object> requestData) {
	    Map<String, Object> response = new HashMap<>();

	    String updatedUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    
	    try {
	        Map<String, Object> contractData = (Map<String, Object>) requestData.get("contract");
	        List<Map<String, Object>> detailData = (List<Map<String, Object>>) requestData.get("details");
	        
	        // 필수 값 추출
	        String contractCd = (String) contractData.get("contract_cd");
	        String contractEd = (String) contractData.get("contract_ed");
	        String contractSt = (String) contractData.get("contract_st");
	        
	        System.out.println("Contract Code: " + contractCd);
	        System.out.println("Contract End Date: " + contractEd);

	        if (contractCd == null || contractEd == null) {
	            response.put("success", false);
	            response.put("message", "계약 코드 또는 출하일이 누락되었습니다.");
	            return response;
	        }

	        // 출하 정보 및 CONTRACTDETAIL 업데이트 실행
	        businessService.updateContractShipment(contractCd, contractEd, contractSt, detailData, updatedUser);

	        response.put("success", true);
	        response.put("message", "출하 업데이트 완료 및 재고 차감 완료");

	    } catch (Exception e) {
	        response.put("success", false);
	        response.put("message", "출하 처리 중 오류 발생: " + e.getMessage());
	    }

	    return response;
	}

}
