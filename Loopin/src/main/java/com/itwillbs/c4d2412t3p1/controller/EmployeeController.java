package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Member;
import com.itwillbs.c4d2412t3p1.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class EmployeeController {

	private final EmployeeService employeeService;
	
	
	@GetMapping("/employee_list")
	public String employee_list() {
		return "/employee/employee_list";
	}

	
	// 인사 카드 조회
	@GetMapping("/select_EMPLOYEE")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_EMPLOYEE() {
	    List<Employee> employees = employeeService.findAll(); // 모든 직원 정보를 가져옵니다.

	    List<Map<String, Object>> response = employees.stream().map(employee -> {
	        Map<String, Object> row = new HashMap<>();
	        row.put("employeeId", employee.getEmployeeId());
	        row.put("employee_nm", employee.getEmployee_nm());
	        row.put("employee_dp", employee.getEmployee_dp());
	        row.put("employee_gd", employee.getEmployee_gd());
	        row.put("employee_bd", employee.getEmployee_bd());
	        row.put("employee_ad", employee.getEmployee_ad());
	        row.put("employee_ph", employee.getEmployee_ph());
	        row.put("employee_em", employee.getEmployee_em());
	        row.put("employee_hd", employee.getEmployee_hd());
	        return row;
	    }).collect(Collectors.toList());

	    return ResponseEntity.ok(response);
	}

	@PostMapping("/insert_EMPLOYEE")
	public ResponseEntity<Map<String, String>> insert_EMPLOYEE(@RequestBody EmployeeDTO employeeDTO) {
		Map<String, String> response = new HashMap<>();
		try {
			employeeService.insert_EMPLOYEE(employeeDTO);
			response.put("message", "데이터가 성공적으로 저장되었습니다.");
			return ResponseEntity.ok(response); // JSON 형식으로 반환
		} catch (Exception e) {
			response.put("message", "데이터 저장 실패: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
//	인사발령 삭제
	@PostMapping("/delete_EMPLOYEE")
	public ResponseEntity<Map<String, Object>> delete_EMPLOYEE(@RequestBody List<String> ids) {
		log.info("삭제 요청 데이터: " + ids.toString());
		
		Map<String, Object> response = new HashMap<>();

		try {
			employeeService.delete_EMPLOYEE(ids); // Service 계층에서 삭제 처리
			response.put("success", true);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
	
}
