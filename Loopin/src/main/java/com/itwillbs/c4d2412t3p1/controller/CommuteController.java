package com.itwillbs.c4d2412t3p1.controller;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteRequestDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Holiday;
import com.itwillbs.c4d2412t3p1.entity.Workinghour;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.AttendanceService;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.CommuteService;
import com.itwillbs.c4d2412t3p1.service.EmployeeService;
import com.itwillbs.c4d2412t3p1.service.UtilService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.CommuteFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import oracle.jdbc.proxy.annotation.Post;

@RequiredArgsConstructor
@Controller
@Log
public class CommuteController {
	
	private final CommuteService commuteService;
	private final EmployeeService employeeService;
	private final AttendanceService attendanceService;
	private final UtilService util;
	

	// 출퇴근 기록부 --------------------------------------------
	@GetMapping("/commute")
	public String commute(Model model) {
	    
		return "/commute/commute_list";
	}
	
	
	// 캘린더 형식 조회(출퇴근 기록부 첫화면)
	@ResponseBody
	@PostMapping("/select_COMMUTE_calendar")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_calendar(@RequestBody CommuteRequestDTO commuteRequest) {
		EmployeeDetails employee = util.getEmployee();
		boolean isAdmin = util.isAuthority("SYS_ADMIN", "AT_ADMIN");
		String startDate = commuteRequest.getCalendarStartDate();
		String EndDate = commuteRequest.getCalendarEndDate();
		String type = commuteRequest.getType();
		CommuteFilterRequest filter = new CommuteFilterRequest();
		filter.setStartDate(startDate);
		filter.setEndDate(EndDate);
		
		List<CommuteDTO> list = commuteService.select_COMMUTE_calendar(employee.getEmployee_cd(), isAdmin, startDate, EndDate, type);
		List<Holiday> holidayList = commuteService.select_HOLIDAY_month(startDate, EndDate); // 공휴일 리스트
		CommuteDTO commute = commuteService.selcet_COMMUTE_time(filter, employee.getEmployee_cd(), isAdmin);

		Map<String, Object> response = new HashMap<>(); 
		response.put("result", true);
		response.put("list", list);
		response.put("holidayList", holidayList);
		response.put("commute", commute);
		
		return ResponseEntity.ok(response);
	}
	
	
	// 캘린더에서 선택한 모달의 그리드
	@ResponseBody
	@GetMapping("/select_COMMUTE_detail")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_detail(@RequestParam(name = "commute_wd", defaultValue = "") String commute_wd) {
		EmployeeDetails employee = util.getEmployee();
		boolean isAdmin = util.isAuthority("SYS_ADMIN", "AT_ADMIN");
		
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
		boolean isEmp = util.isAuthority("EMPLOYEE");
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
	@LogActivity(value = "등록", action = "출퇴근등록")
	@ResponseBody
	@PostMapping("/insert_COMMUTE_modal")
	public ResponseEntity<Map<String, Object>> insert_COMMUTE_modal(@RequestBody CommuteRequestDTO commuteRequest) {
		CommuteDTO employee = commuteRequest.getCommute();
		boolean isAdmin = util.isAuthority("SYS_ADMIN", "AT_ADMIN");
		String startDate = commuteRequest.getCalendarStartDate();
		String EndDate = commuteRequest.getCalendarEndDate();
		String type = commuteRequest.getType();
		CommuteFilterRequest filter = new CommuteFilterRequest();
		filter.setStartDate(startDate);
		filter.setEndDate(EndDate);
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			commuteService.insert_COMMUTE_modal(employee);
			
			List<CommuteDTO> gridList = commuteService.select_COMMUTE_detail(employee.getEmployee_cd(), isAdmin,employee.getCommute_wd());
			List<CommuteDTO> calendarList = commuteService.select_COMMUTE_calendar(employee.getEmployee_cd(), isAdmin, startDate, EndDate, type);
			CommuteDTO commute = commuteService.selcet_COMMUTE_time(filter, employee.getEmployee_cd(), isAdmin);

			response.put("result", true);
			response.put("gridList", gridList);
			response.put("calendarList", calendarList);
			response.put("commute", commute);
			
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
		EmployeeDetails employee = util.getEmployee();
		boolean isAdmin = util.isAuthority("SYS_ADMIN", "AT_ADMIN");
		CommuteFilterRequest filterRequest = commuteRequest.getCommuteFilter();
		String type = commuteRequest.getType();
		
		List<CommuteDTO> list = commuteService.select_COMMUTE_grid(filterRequest, employee.getEmployee_cd(), isAdmin, type);
		CommuteDTO commute = commuteService.selcet_COMMUTE_time(filterRequest, employee.getEmployee_cd(), isAdmin);
		log.info(list.toString());
		
		Map<String, Object> response = new HashMap<>(); 
		response.put("result", true);
		response.put("list", list);
		response.put("commute", commute);
		
		return ResponseEntity.ok(response);
	}
	
	// 출근 일정 조회의 등록(그리드)
	@LogActivity(value = "등록", action = "출퇴근등록")
	@ResponseBody
	@PostMapping("/insert_COMMUTE_grid")
	public ResponseEntity<Map<String, Object>> insert_COMMUTE_grid(@RequestBody CommuteRequestDTO commuteRequest) {
		EmployeeDetails employee = util.getEmployee();
		boolean isAdmin = util.isAuthority("SYS_ADMIN", "AT_ADMIN");
		String type = commuteRequest.getType();
		CommuteFilterRequest filterRequest = commuteRequest.getCommuteFilter();
		
		Map<String, Object> response = new HashMap<>(); 
		try {
			commuteService.insert_COMMUTE_modal(commuteRequest.getCommute());
			
			List<CommuteDTO> gridList = commuteService.select_COMMUTE_grid(filterRequest, employee.getEmployee_cd(), isAdmin, type);
			CommuteDTO commute = commuteService.selcet_COMMUTE_time(filterRequest, employee.getEmployee_cd(), isAdmin);
			
			response.put("result", true);
			response.put("gridList", gridList);
			response.put("commute", commute);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("result", false);
			response.put("msg", e.getMessage());
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
	@LogActivity(value = "등록", action = "근로관리")
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
	@LogActivity(value = "등록", action = "사원 근로 등록")
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
		EmployeeDetails employeeDetails = util.getEmployee();
		String employee_cd = employeeDetails.getEmployee_cd();
		Employee employee = employeeService.findEmployeeById(employee_cd);
		String workinghour_id = employee.getWorkinghour_id();
		String today = LocalDate.now().toString();
		if(workinghour_id == null) {
			response.put("msg", "근무형태를 등록해야합니다.<br>관리자에게 문의하세요.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		LocalTime currentTime = LocalTime.now();
		String annual = util.checkNull(commuteService.select_APPROVAL(employee_cd, today));
		if(annual.equals("AMBN") && currentTime.isBefore(LocalTime.of(12, 0))) {
	        response.put("msg", "오전 반차 기간입니다.<br>시간이 지난 후 출근해주세요.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else if(annual.equals("PMBN") && !isAttendance && currentTime.isBefore(LocalTime.of(12, 0))) {
			response.put("msg", "아직 오후 반차 시간 이전입니다.<br>점심시간 이후 퇴근해주세요.");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else if(annual.equals("AN")) {
			// 연차~
		}
		
		try {
//			Commute commute = commuteService.findById(employee_cd, workinghour_id, today);
//			commute = commuteService.insert_COMMUTE(employee_cd, workinghour_id, commute);	
			
			response.put("msg", (isAttendance ? "출근" : "퇴근") + "하였습니다.");
//			response.put("commute", commute);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("msg", (isAttendance ? "출근" : "퇴근") + "에 실패했습니다.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	

	@PreAuthorize("hasAnyRole('ROLE_SYS_ADMIN', 'ROLE_AT_ADMIN')")
	@GetMapping("/commute_chart")
	public String commuteChart() {		
		
		return "/commute/commute_chart";
	}
	
	
	@PostMapping("/select_COMMUTE_timeList")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_timeList(@RequestBody CommuteRequestDTO commuteRequest) {
		CommuteFilterRequest filter = commuteRequest.getCommuteFilter();
		Map<String, Object> response = new HashMap<>();
		
		try {
			List<CommuteDTO> commuteList = commuteService.select_COMMUTE_timeList(filter);
			response.put("commuteList", commuteList);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("msg", e.getMessage());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@PostMapping("/select_COMMUTE_chartList")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_chartList(@RequestBody CommuteRequestDTO commuteRequest) {
		CommuteFilterRequest filter = commuteRequest.getCommuteFilter();
		Map<String, Object> response = new HashMap<>();
		String sort = "";
		
		try {
			
			// 일자별 근로시간 차트 ------------------------------------------
	        List<CommuteDTO> dayCommuteData = commuteService.select_COMMUTE_dayCommuteChart(filter);
	        Map<String, Object> dayCommuteChart = new HashMap<>();
	        
	        List<String> dayCommuteCategories = dayCommuteData.stream()
	            .map(CommuteDTO::getCommute_wd)
	            .collect(Collectors.toList());
	            
	        List<Map<String, Object>> dayCommuteSeries = Arrays.asList(
        		commuteService.createSeriesData("일반근무", CommuteDTO::getCommute_ig, dayCommuteData),
        		commuteService.createSeriesData("연장근무", CommuteDTO::getCommute_eg, dayCommuteData),
        		commuteService.createSeriesData("야간근무", CommuteDTO::getCommute_yg, dayCommuteData),
        		commuteService.createSeriesData("주말근무", CommuteDTO::getCommute_jg, dayCommuteData),
        		commuteService.createSeriesData("휴일근무", CommuteDTO::getCommute_hg, dayCommuteData)
	        );
	        
	        dayCommuteChart.put("categories", dayCommuteCategories);
	        dayCommuteChart.put("series", dayCommuteSeries);
	        response.put("dayCommuteChart", dayCommuteChart);
	        
	        // 근로시간 차트 ------------------------------------------	
	        List<CommuteDTO> commuteData = commuteService.select_COMMUTE_commuteChart(filter);
	        Map<String, Object> commuteChart = new HashMap<>();
	        List<String> commuteCategories = Arrays.asList("근무시간");		            
	        List<Map<String, Object>> commuteSeries = Arrays.asList(
        		commuteService.createSeriesData("일반근무", CommuteDTO::getCommute_ig, commuteData),
        		commuteService.createSeriesData("연장근무", CommuteDTO::getCommute_eg, commuteData),
        		commuteService.createSeriesData("야간근무", CommuteDTO::getCommute_yg, commuteData),
        		commuteService.createSeriesData("주말근무", CommuteDTO::getCommute_jg, commuteData),
        		commuteService.createSeriesData("휴일근무", CommuteDTO::getCommute_hg, commuteData)
	        );
	        
	        commuteChart.put("categories", commuteCategories);
	        commuteChart.put("series", commuteSeries);
	        response.put("commuteChart", commuteChart);
	        
	        // 직급별 바 차트 ------------------------------------------
	        sort = "POSITION";
	        List<CommuteDTO> gradeData = commuteService.select_COMMUTE_barChart(sort, filter);
	        Map<String, Object> gradeChart = new HashMap<>();
	        
	        List<String> gradeCategories = gradeData.stream()
	            .map(CommuteDTO::getEmployee_gd)
	            .collect(Collectors.toList());
	            
	        List<Map<String, Object>> gradeSeries = Arrays.asList(
	        		commuteService.createSeriesData("정상출근", CommuteDTO::getNormal, gradeData),
	        		commuteService.createSeriesData("지각", CommuteDTO::getLate, gradeData)
	        );
	        
	        gradeChart.put("categories", gradeCategories);
	        gradeChart.put("series", gradeSeries);
	        response.put("gradeChart", gradeChart);
	        
	        // 부서별 바 차트 ------------------------------------------
	        sort = "DEPARTMENT";
	        List<CommuteDTO> deptData = commuteService.select_COMMUTE_barChart(sort, filter);
	        Map<String, Object> deptChart = new HashMap<>();
	        System.out.println("DEPARTMENT : " + deptData.toString());
	        List<String> deptCategories = deptData.stream()
	            .map(CommuteDTO::getEmployee_gd)
	            .collect(Collectors.toList());
	            
	        List<Map<String, Object>> deptSeries = Arrays.asList(
	        		commuteService.createSeriesData("정상출근", CommuteDTO::getNormal, deptData),
	        		commuteService.createSeriesData("지각", CommuteDTO::getLate, deptData)
	        );
	        
	        deptChart.put("categories", deptCategories);
	        deptChart.put("series", deptSeries);
	        response.put("deptChart", deptChart);
	        
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("msg", e.getMessage());
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
	
	
	
	
	@ResponseBody
	@PostMapping("/insert_COMMUTE_list")
	public ResponseEntity<Map<String, Object>> insert_COMMUTE_list(@RequestBody CommuteRequestDTO commuteRequest) {
		String[] arr = new String[] {"EM25-0010", "EM25-0071", "EM25-0011", "EM25-0044", "EM25-0042", "EM25-0014", "EM25-0039",
				"EM25-0067", "EM25-0034", "EM25-0037", "EM25-0041", "EM25-0005", "EM25-0038", "EM25-0026", "EM25-0007", "EM25-0043", "EM25-0040"};
		Map<String, Object> response = new HashMap<>();
		List<String> employee_list = commuteService.select_EMPLOYEE_CD_list();
		
		System.out.println(commuteRequest.getDay());
		System.out.println(commuteRequest.getTime());
		System.out.println(employee_list.toString());
		commuteService.insert_COMMUTE_list(employee_list, commuteRequest.getDay(), commuteRequest.getTime());	
		
		return ResponseEntity.ok(response);
	}

	
}