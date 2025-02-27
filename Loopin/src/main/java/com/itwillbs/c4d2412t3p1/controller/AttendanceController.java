package com.itwillbs.c4d2412t3p1.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.HolidayDTO;
import com.itwillbs.c4d2412t3p1.entity.Attendance;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Holiday;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.AttendanceService;
import com.itwillbs.c4d2412t3p1.service.CommuteService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	private final CommuteService commuteService;
	
	@GetMapping("/attendance_list")
	public String attendance_list() {
		
		return "attendance/attendance_list";
	}
	
	@ResponseBody
	@GetMapping("/select_ATTENDANCE")
	public ResponseEntity<Map<String, Object>> select_ATTENDANCE() {
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			List<Map<String, Object>> annuals = attendanceService.select_ATTENDANCE();
			response.put("result", true);
			response.put("data", annuals);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@GetMapping("/annual_regist")
	public String annual_regist() {
		
		return "attendance/annual_regist";
	}
	
	@ResponseBody
	@GetMapping("/select_EMPLOYEE_ANNUAL")
	public ResponseEntity<List<Map<String, Object>>> select_EMPLOYEE_ANNUAL(@RequestParam("employee_nm") String employee_nm) {
	    log.info(employee_nm + " select_EMPLOYEE 조회 시도");

	    // 서비스 호출로 데이터를 가져옴
	    List<Map<String, Object>> employees = attendanceService.select_EMPLOYEE_ANNUAL(employee_nm);

	    // 데이터 가공
	    List<Map<String, Object>> response = employees.stream().map(employee -> {
	        Map<String, Object> row = new HashMap<>();

	        // 키를 소문자로 변환하여 새로운 맵에 저장
	        for (Entry<String, Object> entry : employee.entrySet()) {
	            String key = entry.getKey().toLowerCase();
	            Object value = entry.getValue();

	            // Oracle TIMESTAMP -> java.sql.Timestamp 변환
	            if (value instanceof oracle.sql.TIMESTAMP) {
	                try {
	                    value = ((oracle.sql.TIMESTAMP) value).timestampValue(); // Oracle TIMESTAMP를 java.sql.Timestamp로 변환
	                } catch (SQLException e) {
//	                    log.error("Failed to convert Oracle TIMESTAMP to java.sql.Timestamp", e);
	                    value = null; // 변환 실패 시 null 처리
	                }
	            }

	            // row에 변환된 값 추가
	            row.put(key, value);
	        }

	        return row;
	    }).collect(Collectors.toList());

	    // 최종 응답 반환
	    return ResponseEntity.ok(response);
	}

	@ResponseBody
	@GetMapping("/select_filer_ANNUAL")
	public ResponseEntity<Map<String, Object>> select_filer_ANNUAL(AttendanceDTO attendanceDTO) {
	    System.out.println("받은 데이터: " + attendanceDTO); // 디버깅용
	    
	    // 로직 처리 후 성공 응답
	    Map<String, Object> response = new HashMap<>(); 
		try {
			List<Map<String, Object>> annuals = attendanceService.select_filer_ANNUAL(attendanceDTO);
			
			response.put("result", true);
			response.put("data", annuals);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	    
	}
	
	@GetMapping("/holiday_regist")
	public String holiday_regist() {
		return "/attendance/holiday_regist";
	}
	
	@ResponseBody
	@GetMapping("/select_HOLIDAY")
	public ResponseEntity<List<Holiday>> select_HOLIDAY() {
	    List<Holiday> list = attendanceService.select_HOLIDAY();

	    return ResponseEntity.ok(list);
	}
	

	@ResponseBody
	@GetMapping("/select_period_HOLIDAY")
	public ResponseEntity<List<Map<String, Object>>> select_period_HOLIDAY(@RequestParam("holiday_dt1") String holiday_dt1, @RequestParam("holiday_dt2") String holiday_dt2) {
		log.info(holiday_dt1 + holiday_dt2 + " select_period_HOLIDAY 조회 시도");
		
		List<Map<String, Object>> list = attendanceService.select_period_HOLIDAY(holiday_dt1, holiday_dt2);
		List<Map<String, Object>> response = list.stream().map(holiday -> {
	        Map<String, Object> row = new HashMap<>();

	        // 키를 소문자로 변환하여 새로운 맵에 저장
	        for (Entry<String, Object> entry : holiday.entrySet()) {
	            String key = entry.getKey().toLowerCase();
	            Object value = entry.getValue();

	            // Oracle TIMESTAMP -> java.sql.Timestamp 변환
	            if (value instanceof oracle.sql.TIMESTAMP) {
	                try {
	                    value = ((oracle.sql.TIMESTAMP) value).timestampValue(); // Oracle TIMESTAMP를 java.sql.Timestamp로 변환
	                } catch (SQLException e) {
//	                    log.error("Failed to convert Oracle TIMESTAMP to java.sql.Timestamp", e);
	                    value = null; // 변환 실패 시 null 처리
	                }
	            }

	            // row에 변환된 값 추가
	            row.put(key, value);
	        }

	        return row;
	        
	    }).collect(Collectors.toList());

	    // 최종 응답 반환
	    return ResponseEntity.ok(response);
	}
	
	@LogActivity(value = "입력", action = "공휴일등록")
	@ResponseBody
	@PostMapping("/insert_HOLIDAY")
    public ResponseEntity<Map<String, Object>> insert_HOLIDAY(@RequestBody List<Map<String, String>> holidays) {
		
		Map<String, Object> response = new HashMap<>(); 
		
		try {
			attendanceService.insert_HOLIDAY(holidays);
			
			List<Holiday> data = attendanceService.select_HOLIDAY();
			
			response.put("result", true);
			response.put("data", data);
			log.info("response" + response);
			return ResponseEntity.ok(response);
				
		} catch (Exception e) {
			
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
    }
	
	// 휴가 등록
	@LogActivity(value = "입력", action = "연차등록")
	@ResponseBody
	@PostMapping("/insert_ANNUAL")
	public ResponseEntity<Map<String, Object>> insert_ANNUAL(@RequestBody String annual_yr) {
		
		
		List<Map<String, Object>> annuals = attendanceService.select_ATTENDANCE();
		
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			attendanceService.insert_ANNUAL(annuals);
			
			List<Map<String, Object>> data = attendanceService.select_ATTENDANCE();
			
			response.put("result", true);
			response.put("data", data);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		
		
	}
	
	@LogActivity(value = "입력", action = "회사휴일등록")
	@ResponseBody
	@PostMapping("/insert_company_HOLIDAY")
	public Map<String, Object> insert_company_HOLIDAY(@RequestBody Map<String, Object> requestData) {
	    // "createdRows"와 "updatedRows" 추출
	    List<Map<String, String>> createdRows = (List<Map<String, String>>) requestData.get("createdRows");
	    List<Map<String, String>> updatedRows = (List<Map<String, String>>) requestData.get("updatedRows");
	    
	    // 디버깅 로그
	    log.info("생성 항목: {}"+ createdRows);
	    log.info("수정 항목: {}"+ updatedRows);

	    int createdCount = 0;
	    int updatedCount = 0;

	    // 생성 및 수정 처리
	    try {
	        if (createdRows != null && !createdRows.isEmpty()) {
	            createdCount = attendanceService.insertCompanyHoliday(createdRows);
	        }

	        if (updatedRows != null && !updatedRows.isEmpty()) {
	            updatedCount = attendanceService.updateCompanyHoliday(updatedRows);
	        }
	    } catch (Exception e) {
	        log.info("휴일 데이터 처리 실패: {}"+ e.getMessage());
	        Map<String, Object> response = new HashMap<>();
	        response.put("result", false);
	        response.put("error", "데이터 처리 중 오류 발생");
	        return response;
	    }

	    // 결과 응답 생성
	    Map<String, Object> response = new HashMap<>();
	    response.put("result", createdCount > 0 || updatedCount > 0);

	    if (createdCount > 0 || updatedCount > 0) {
	        List<Holiday> codeList = attendanceService.select_HOLIDAY();
	        Map<String, Object> data = new HashMap<>();
	        data.put("contents", codeList);
	        response.put("data", data);
	    }

	    return response;
	}
	
	@LogActivity(value = "삭제", action = "회사휴일삭제")
	@ResponseBody
	@PostMapping("/delete_company_HOLIDAY")
	public Map<String, Object> delete_company_HOLIDAY(@RequestBody List<Map<String, Object>> requestData) {

	    // requestData에서 "holiday_dt" 값 추출
	    List<String> deletedRows = requestData.stream()
	            .map(row -> (String) row.get("holiday_dt")) // 각 Map에서 "holiday_dt" 값 추출
	            .collect(Collectors.toList());

	    int deleteCount = 0;

	    if (deletedRows != null && !deletedRows.isEmpty()) {
	        deleteCount = attendanceService.delete_company_HOLIDAY(deletedRows);
	    }

	    Map<String, Object> response = new HashMap<>();
	    response.put("result", deleteCount);

	    if (deleteCount > 0) {
	        List<Holiday> codeList = attendanceService.select_HOLIDAY();
	        Map<String, Object> data = new HashMap<>();
	        data.put("contents", codeList);
	        response.put("data", data);
	    }

	    return response;
	}
	
	
	@ResponseBody
	@PostMapping("/select_APPROVAL_ANNUAL")
	public ResponseEntity<Map<String, Object>> select_APPROVAL_ANNUAL(@RequestBody Map<String, Object> params) {
		
		EmployeeDetails employee = commuteService.getEmployee();
		
		
		params.put("isAdmin", commuteService.isAuthority("SYS_ADMIN", "AT_ADMIN"));
		params.put("employee_cd", employee.getEmployee_cd());		
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			
			List<Map<String, Object>> annuals = attendanceService.select_APPROVAL_ANNUAL(params);
			response.put("result", true);
			response.put("data", annuals);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@ResponseBody
	@PostMapping("/select_calendar_ANNUAL")
	public ResponseEntity<Map<String, Object>> select_calendar_ANNUAL(@RequestBody Map<String, Object> params) {
		
		log.info("type"+params);
//		
//		if(type == 1) {
//			params.put("type", 1);
//		}
		
		EmployeeDetails employee = commuteService.getEmployee();
		
		List<Map<String, Object>> holidayList = attendanceService.select_period_HOLIDAY((String)params.get("holiday_dt1"),(String)params.get("holiday_dt2"));
		
		
		params.put("isAdmin", commuteService.isAuthority("SYS_ADMIN", "AT_ADMIN"));
		params.put("employee_cd", employee.getEmployee_cd());		
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			List<Map<String, Object>> data = attendanceService.select_calendar_ANNUAL(params);
			
			response.put("result", true);
			response.put("holidayList", holidayList);
			response.put("data", data);
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@ResponseBody
	@GetMapping("/select_detail_ANNUAL")
	public ResponseEntity<Map<String, Object>> select_detail_ANNUAL(@RequestParam Map<String, Object> params) {
		
		EmployeeDetails employee = commuteService.getEmployee();
		
		params.put("isAdmin", commuteService.isAuthority("SYS_ADMIN", "AT_ADMIN"));
		params.put("employee_cd", employee.getEmployee_cd());		
		
		log.info("type"+params);
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			List<Map<String, Object>> list = attendanceService.select_calendar_ANNUAL(params);
			Map<String, Object> data = new HashMap<>();
			data.put("contents", list);
			response.put("result", true);
			response.put("data", data);
			return ResponseEntity.ok(response);
			
		} catch (Exception e) {
			
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
}