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

import com.itwillbs.c4d2412t3p1.domain.OrderDTO;
import com.itwillbs.c4d2412t3p1.domain.OrderDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.OrderRequestDTO;
import com.itwillbs.c4d2412t3p1.service.BusinessService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class BusinessController {

	private final BusinessService businessService ;
	
	
	// 발주관리 페이지로 이동
	@GetMapping("/order_list")
	public String order_list(Model model) {
		
		// 수주상태
		model.addAttribute("status_list", businessService.selectCommonList("STATUS"));
		
		return "/business/order_list";
	}
	
	// 원자재 조회
	@GetMapping("/select_MATERIAL")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_MATERIAL() {
		
		try {
	    	List<Map<String, Object>> response = businessService.select_MATERIAL();
	    	
	    	return ResponseEntity.ok(response);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	    
	}

	// 발주 조회
	@GetMapping("/select_ORDER")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_ORDER() {
		
		try {
			List<Map<String, Object>> response = businessService.select_ORDER();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
		
	}

	// 발주상세 조회
	@GetMapping("/select_ORDERDETAIL")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_ORDERDETAIL(
			@RequestParam(name = "order_cd") String orderCd) {
		
		try {
			List<Map<String, Object>> details = businessService.select_ORDERDETAIL(orderCd);
			
			return ResponseEntity.ok(details);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
	}

	// 거래처 조회
	@GetMapping("/select_ACCOUNT_ORDER")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_ACCOUNT_ORDER() {
		
		try {
			List<Map<String, Object>> response = businessService.select_ACCOUNT_ORDER();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
		
	}

	// 담당자 조회
	@GetMapping("/select_ORDER_PS")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_ORDER_PS() {
		
		try {
			List<Map<String, Object>> response = businessService.select_ORDER_PS();
			
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}
		
	}
	
	@ResponseBody
	@PostMapping("/insert_ORDER")
	public ResponseEntity<Map<String, String>> insert_ORDER(
			@RequestBody OrderRequestDTO orderRequestDTO
	) {
	    Map<String, String> response = new HashMap<>();
	    
	    
	    // 시큐리티 세션 값 가져오기
	    String employee_id = SecurityContextHolder.getContext().getAuthentication().getName(); 
	    
	    try {
	    	
	    	OrderDTO orderDto = orderRequestDTO.getContract();
	    	orderDto.setOrder_wr(employee_id);
	    	orderDto.setOrder_wd(new Timestamp(System.currentTimeMillis()));
	    	
	    	List<OrderDetailDTO> details = orderRequestDTO.getDetails();

	        businessService.insert_ORDER(orderDto, details);

	        response.put("message", "데이터가 성공적으로 저장되었습니다.");
	        return ResponseEntity.ok(response); // JSON 형식으로 반환
	    } catch (Exception e) {
	        log.severe("데이터 저장 실패: " + e.getMessage()); // 오류 로그 추가
	        response.put("message", "데이터 저장 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	@GetMapping("/get_ORDER")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> getOrderDetails(@RequestParam(name = "order_cd") String orderCd) {
	    try {
	        // 서비스에서 계약 정보 + 상세 정보 조회
	        Map<String, Object> orderData = businessService.getOrderDetails(orderCd);

	        if (orderData.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(Map.of("error", "계약 데이터를 찾을 수 없습니다."));
	        }

	        return ResponseEntity.ok(orderData);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "데이터 조회 중 오류 발생"));
	    }
	}

	@ResponseBody
	@PostMapping("/update_ORDER")
	public ResponseEntity<Map<String, String>> update_ORDER(
	        @RequestBody OrderRequestDTO orderRequestDTO
	) {
	    Map<String, String> response = new HashMap<>();

	    // 시큐리티 세션 값 가져오기
	    String employee_id = SecurityContextHolder.getContext().getAuthentication().getName();

	    try {
	        OrderDTO orderDto = orderRequestDTO.getContract();
	        
	        orderDto.setOrder_mf(employee_id); // 수정자 설정
	        orderDto.setOrder_md(new Timestamp(System.currentTimeMillis())); // 수정 시간 설정

	        List<OrderDetailDTO> details = orderRequestDTO.getDetails();

	        // 수정 서비스 호출
	        businessService.update_ORDER(orderDto, details);

	        response.put("message", "수정이 성공적으로 완료되었습니다.");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        log.severe("수정 실패: " + e.getMessage());
	        response.put("message", "수정 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}


	
	
}
