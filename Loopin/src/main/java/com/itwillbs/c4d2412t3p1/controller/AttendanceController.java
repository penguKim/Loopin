package com.itwillbs.c4d2412t3p1.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;
import com.itwillbs.c4d2412t3p1.entity.Attendance;
import com.itwillbs.c4d2412t3p1.service.AttendanceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	
	@GetMapping("/attendance_list")
	public String attendance_list() {
		
		return "attendance/attendance_list";
	}
	
	@ResponseBody
	@GetMapping("/select_base_ATTENDANCE")
	public ResponseEntity<List<Map<String, Object>>> select_base_ATTENDANCE() {
		log.info("조회 시도");
		List<Attendance> attendances = attendanceService.findAll();
		
		List<Map<String, Object>> response = attendances.stream().map(attendance -> {
			Map<String, Object> row = new HashMap<>();
			
			row.put("annual_id", attendance.getAnnual_id());
			row.put("employee_cd", attendance.getEmployee_cd());
			row.put("annual_cc", attendance.getAnnual_cc());
			row.put("annual_yr", attendance.getAnnual_yr());
			row.put("annual_ua", attendance.getAnnual_ua());
			row.put("annual_ra", attendance.getAnnual_ra());
			row.put("annual_aa", attendance.getAnnual_aa());
			row.put("annual_ru", attendance.getAnnual_ru());
			row.put("annual_rd", attendance.getAnnual_rd());
			row.put("annual_uu", attendance.getAnnual_uu());
			row.put("annual_ud", attendance.getAnnual_ud());
			
			return row;
			
		}).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/annual_regist")
	public String annual_regist() {
		
		return "attendance/annual_regist";
	}
	
	@ResponseBody
	@PostMapping("/insert_ANNUAL")
	public ResponseEntity<Map<String, String>> insert_ANNUAL(@RequestBody Map<String, Object> request) {
		log.info(request.get("annual_yr") + "오늘날짜");
		
		String annual_yr =  request.get("annual_yr").toString();
		
		List<Attendance> select_ANNUAL = attendanceService.select_ANNUAL(annual_yr); //사원코드, 사용연도, 잔여연차, 총연차
		
		log.info(select_ANNUAL + "유효값"); 
		
		
		
		Map<String, String> response = new HashMap<>();
		try {
			attendanceService.insert_ANNUAL();
			response.put("message", "데이터가 성공적으로 저장되었습니다.");
			return ResponseEntity.ok(response); // JSON 형식으로 반환
		} catch (Exception e) {
			response.put("message", "데이터 저장 실패: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
//	@GetMapping("/select_EMPLOYEE")
//	public ResponseEntity<Map<String, String>> select_EMPLOYEE(@RequestParam("employee_id") String employee_id) {
//		 try {
//	            Employee employee = employeeService.findByName(name);
//	            if (employee == null) {
//	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사원을 찾을 수 없습니다.");
//	            }
//	            Map<String, Object> response = new HashMap<>();
//	            response.put("department", employee.getDepartment());
//	            response.put("hireDate", employee.getHireDate());
//	            response.put("remainingAnnual", employee.getRemainingAnnual());
//	            return ResponseEntity.ok(response);
//	        } catch (Exception e) {
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생");
//	        }
//	    }
//	}

	
}
