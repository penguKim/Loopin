package com.itwillbs.c4d2412t3p1.controller;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.domain.WorktypeDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Holiday;
import com.itwillbs.c4d2412t3p1.entity.Workinghour;
import com.itwillbs.c4d2412t3p1.mapper.CommuteMapper;
import com.itwillbs.c4d2412t3p1.repository.WorkinghourRepository;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.CommuteService;
import com.itwillbs.c4d2412t3p1.service.EmployeeService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.CommuteFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class CommuteController {
	
	private final CommuteService commuteService;
	private final CommonService commonService;
	

	// 출퇴근 기록부 --------------------------------------------
	@GetMapping("/commute")
	public String commute(Model model) {

	    
		return "/commute/commute_list";
	}
	
	
	// 캘린더 형식 조회(출퇴근 기록부 첫화면)
	@ResponseBody
	@PostMapping("/select_COMMUTE_calendar")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_calendar(@RequestBody CommuteRequestDTO commuteRequest) {
		EmployeeDetails employee = commuteService.getEmployee();
		boolean isAdmin = commuteService.isAuthority("SYS_ADMIN", "AT_ADMIN");
		String startDate = commuteRequest.getCalendarStartDate();
		String EndDate = commuteRequest.getCalendarEndDate();
		
		List<CommuteDTO> list = commuteService.select_COMMUTE_calendar(employee.getEmployee_cd(), isAdmin, startDate, EndDate);
		List<Holiday> holidayList = commuteService.select_HOLIDAY_month(startDate, EndDate); // 공휴일 리스트
		
		Map<String, Object> response = new HashMap<>(); 
		response.put("result", true);
		response.put("list", list);
		response.put("holidayList", holidayList);
		
		return ResponseEntity.ok(response);
	}
	
	
	// 캘린더에서 선택한 모달의 그리드
	@ResponseBody
	@GetMapping("/select_COMMUTE_detail")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_detail(@RequestParam(name = "commute_wd", defaultValue = "") String commute_wd) {
		EmployeeDetails employee = commuteService.getEmployee();
		boolean isAdmin = commuteService.isAuthority("SYS_ADMIN", "AT_ADMIN");
		
		List<CommuteDTO> list = commuteService.select_COMMUTE_detail(employee.getEmployee_cd(), isAdmin, commute_wd);
		
		Map<String, Object> response = new HashMap<>(); 
		response.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		response.put("data", data);
		
		return ResponseEntity.ok(response);
	}
	
	// 출근일정등록의 미출근 사원 조회
	@ResponseBody
	@GetMapping("/select_EMPLOYEE_grid")
	public ResponseEntity<Map<String, Object>> select_EMPLOYEE_grid(@RequestParam(name = "commute_wd", defaultValue = "") String commute_wd) {
		Map<String, Object> response = new HashMap<>(); 
		boolean isEmp = commuteService.isAuthority("EMPLOYEE");
		if(isEmp) {
	        response.put("result", false);
	        response.put("message", "관리자 권한이 필요합니다.");
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
		}
		List<CommuteDTO> list = commuteService.select_EMPLOYEE_grid(commute_wd);
		
		response.put("result", true);
		Map<String, Object> data = new HashMap<>();
		data.put("contents", list);
		response.put("data", data);
		
		return ResponseEntity.ok(response);
	}
	
	// 출근 일정 조회의 등록모달
	@ResponseBody
	@PostMapping("/insert_COMMUTE_modal")
	public ResponseEntity<Map<String, Object>> insert_COMMUTE_modal(@RequestBody CommuteRequestDTO commuteRequest) {
		CommuteDTO commute = commuteRequest.getCommute();
		boolean isAdmin = commuteService.isAuthority("SYS_ADMIN", "AT_ADMIN");
		String startDate = commuteRequest.getCalendarStartDate();
		String EndDate = commuteRequest.getCalendarEndDate();
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			commuteService.insert_COMMUTE_modal(commute);
			
			List<CommuteDTO> gridList = commuteService.select_COMMUTE_detail(commute.getEmployee_cd(), isAdmin,commute.getCommute_wd());
			List<CommuteDTO> calendarList = commuteService.select_COMMUTE_calendar(commute.getEmployee_cd(), isAdmin, startDate, EndDate );
			
			response.put("result", true);
			response.put("gridList", gridList);
			response.put("calendarList", calendarList);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 그리드 탭 조회
	@ResponseBody
	@PostMapping("/select_COMMUTE_grid")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_grid(@RequestBody CommuteRequestDTO commuteRequest) {
		EmployeeDetails employee = commuteService.getEmployee();
		boolean isAdmin = commuteService.isAuthority("SYS_ADMIN", "AT_ADMIN");
		CommuteFilterRequest filterRequest = commuteRequest.getCommuteFilter();
		
		List<CommuteDTO> list = commuteService.select_COMMUTE_grid(filterRequest, employee.getEmployee_cd(), isAdmin);
		log.info(list.toString());
		
		Map<String, Object> response = new HashMap<>(); 
		response.put("result", true);
		response.put("list", list);
		
		return ResponseEntity.ok(response);
	}
	
	// 출근 일정 조회의 등록(그리드)
	@ResponseBody
	@PostMapping("/insert_COMMUTE_grid")
	public ResponseEntity<Map<String, Object>> insert_COMMUTE_grid(@RequestBody CommuteRequestDTO commuteRequest) {
		EmployeeDetails employee = commuteService.getEmployee();
		boolean isAdmin = commuteService.isAuthority("SYS_ADMIN", "AT_ADMIN");
		CommuteFilterRequest filterRequest = commuteRequest.getCommuteFilter();
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			commuteService.insert_COMMUTE_modal(commuteRequest.getCommute());
			
			List<CommuteDTO> gridList = commuteService.select_COMMUTE_grid(filterRequest, employee.getEmployee_cd(), isAdmin);
			
			response.put("result", true);
			response.put("gridList", gridList);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
	@ResponseBody
	@PostMapping("/select_COMMON_list")
	public ResponseEntity<Map<String, Object>> select_COMMON_list(@RequestBody List<String> list) {
		
		Map<String, Object> response = new HashMap<>(); 
		Map<String, List<Common_codeDTO>> commonList =  commonService.select_COMMON_list(list);
		log.info(commonList.toString());

		if(commonList.size() > 0) {
			response.put("result", true);
			response.put("list", commonList);
			
			return ResponseEntity.ok(response);
		} else {
			response.put("result", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	// 근로 관리 --------------------------------------------
	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_AT_ADMIN')")
	@GetMapping("/commute_type")
	public String commute_type() {
		
		return "commute/commute_type";
	}
	
	// 근로관리 그리드 조회
	@ResponseBody
	@GetMapping("/select_WORKINGHOUR")
	public ResponseEntity<Map<String, Object>> getMethodName() {
		
		List<WorkinghourDTO> list = commuteService.select_WORKINGHOUR();
		log.info(list.toString());
		
		Map<String, Object> response = new HashMap<>(); 
	    response.put("result", true);
	    Map<String, Object> data = new HashMap<>();
	    data.put("contents", list);
	    response.put("data", data);
	    
		return ResponseEntity.ok(response);
	}
	
	// 근로관리 상세 항목 조회
	@ResponseBody
	@PostMapping("/select_WORKINGHOUR_detail")
	public ResponseEntity<Map<String, Object>> select_WORKINGHOUR_detail(@RequestParam("workinghour_id") String workinghour_id) {
		Map<String, Object> response = new HashMap<>();
		try {
			Workinghour workinghour = commuteService.select_WORKINGHOUR_detail(workinghour_id);
			List<Common_codeDTO> weekList = commuteService.select_COMMON_list("WEEK");
			response.put("WEEK", weekList);
			
			response.put("result", true);
			response.put("workinghour", workinghour);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	// 근로관리 항목 등록
	@ResponseBody
	@PostMapping("/insert_WORKINGHOUR")
	public ResponseEntity<Map<String, Object>> insert_WORKINGHOUR(@RequestBody WorkinghourDTO workinghourDTO) {
		
		Map<String, Object> response = new HashMap<>();
		try {
			Workinghour workinghour = commuteService.insert_WORKINGHOUR(workinghourDTO);
			
			List<WorkinghourDTO> list = commuteService.select_WORKINGHOUR();
			response.put("result", true);
			response.put("list", list);
			response.put("workinghour", workinghour);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
	}
	
	// 사원추가 모달 조회
	@ResponseBody
	@PostMapping("/select_EMPLOYEE_WORKINGHOUR")
	public ResponseEntity<Map<String, Object>> select_EMPLOYEE_WORKINGHOUR(@RequestParam("workinghour_id") String workinghour_id) {
		Map<String, Object> response = new HashMap<>();
		try {
			
			List<WorkinghourDTO> EMPLOYEE_LIST = commuteService.select_EMPLOYEE_WORKINGHOUR(""); // 미등록 사원
			List<WorkinghourDTO> CHK_LSIT = commuteService.select_EMPLOYEE_WORKINGHOUR(workinghour_id); // 등록 사원
			
			response.put("result", true);
			response.put("EMPLOYEE_LIST", EMPLOYEE_LIST);
			response.put("CHK_LSIT", CHK_LSIT);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	// 미등록 사원 근로 등록
	@ResponseBody
	@PostMapping("/update_EMPLOYEE_WK")
	public ResponseEntity<Map<String, Object>> update_EMPLOYEE_WK(@RequestBody CommuteRequestDTO commuteRequest) {
		Map<String, Object> response = new HashMap<>();
		try {
			
			List<WorkinghourDTO> WorkinghourList = commuteRequest.getWorkinghourList();
			log.info(WorkinghourList.toString());
			String workinghour_id = commuteRequest.getWorkinghour_id();
            int updateCount = commuteService.update_EMPLOYEE_WK(WorkinghourList, workinghour_id);
            System.out.println("업데이트 갯수 : " + updateCount);
            if(updateCount < 0) {
            	response.put("result", false);
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);            	
            }
		
            List<WorkinghourDTO> EMPLOYEE_LIST = commuteService.select_EMPLOYEE_WORKINGHOUR(""); // 미등록 사원
            List<WorkinghourDTO> CHK_LSIT = commuteService.select_EMPLOYEE_WORKINGHOUR(workinghour_id); // 등록 사원	            	
            response.put("result", true);
			response.put("EMPLOYEE_LIST", EMPLOYEE_LIST);
			response.put("CHK_LSIT", CHK_LSIT);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
	// 출근 등록 -----------------------------------------------
	@ResponseBody
	@PostMapping("/insert_COMMUTE")
	public ResponseEntity<Map<String, Object>> insert_COMMUTE(@RequestBody CommuteRequestDTO commuteRequest) {
		boolean isAttendance = commuteRequest.isAttendance(); // 출근여부
		Map<String, Object> response = new HashMap<>();
		EmployeeDetails employeeDetails = commuteService.getEmployee();
		String employee_cd = employeeDetails.getEmployee_cd();
		String workinghour_id = employeeDetails.getWorkinghour_id();
		if(workinghour_id == null) {
			response.put("result", false);
			response.put("msg", "근무형태를 등록해야합니다.<br>관리자에게 문의하세요.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		
		try {
			Commute commute = commuteService.findById(employee_cd, workinghour_id);
			commute = commuteService.insert_COMMUTE(employee_cd, workinghour_id, commute);	
			
			response.put("result", true);
			response.put("msg", (isAttendance ? "출근" : "퇴근") + "하였습니다.");
			response.put("commute", commute);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", (isAttendance ? "출근" : "퇴근") + "에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// -------------------------- 미사용 ---------------------------
	// 근로정보 관리 --------------------------------------------
	
	@GetMapping("/worktype")
	public String worktype() {
		
		return "commute/work_type";
	}
	
//	@GetMapping("/select_WORKTYPE")
//	public ResponseEntity<Map<String, Object>> select_WORKTYPE() {
//		
//		List<WorktypeDTO> list = commuteService.select_WORKTYPE();
//		
//		Map<String, Object> response = new HashMap<>(); 
//	    response.put("result", true);
//	    Map<String, Object> data = new HashMap<>();
//	    data.put("contents", list);
//	    response.put("data", data);
//		
//		
//		return ResponseEntity.ok(response);
//	}
	
	
	
}