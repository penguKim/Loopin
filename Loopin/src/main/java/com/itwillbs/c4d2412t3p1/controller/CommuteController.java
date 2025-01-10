package com.itwillbs.c4d2412t3p1.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
import com.itwillbs.c4d2412t3p1.domain.CommuteResponseDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.domain.WorktypeDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Workinghour;
import com.itwillbs.c4d2412t3p1.mapper.CommuteMapper;
import com.itwillbs.c4d2412t3p1.repository.WorkinghourRepository;
import com.itwillbs.c4d2412t3p1.service.CommonService;
import com.itwillbs.c4d2412t3p1.service.CommuteService;
import com.itwillbs.c4d2412t3p1.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class CommuteController {
	
	private final CommuteService commuteService;
	private final CommonService commonService;
	private final EmployeeService employeeService;
	

	// 출퇴근 기록부 --------------------------------------------
	
	@GetMapping("/commute")
	public String commute() {
		return "/commute/commute_list";
	}
	
	@ResponseBody
	@GetMapping("/select_COMMUTE_detail")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_detail(@RequestParam(name = "commute_wd", defaultValue = "") String commute_wd) {
		
		System.out.println("---------------------------------------- 파라미터 : " + commute_wd);
		
		List<Commute> list = commuteService.select_COMMUTE_detail(commute_wd);
		
		
		Map<String, Object> response = new HashMap<>(); 
	    response.put("result", true);
	    Map<String, Object> data = new HashMap<>();
	    data.put("contents", list);
	    response.put("data", data);
	    
		
		return ResponseEntity.ok(response);
	}
	
	
	@ResponseBody
	@PostMapping("/select_COMMUTE_list")
	public ResponseEntity<Map<String, Object>> select_COMMUTE_list() {
		
		List<CommuteDTO> list = commuteService.select_COMMUTE_list();
		log.info(list.toString());
		
		Map<String, Object> response = new HashMap<>(); 
		response.put("result", true);
		response.put("list", list);
		
		return ResponseEntity.ok(response);
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
	public ResponseEntity<Map<String, Object>> update_EMPLOYEE_WK(@RequestBody CommuteResponseDTO responseDTO) {
		Map<String, Object> response = new HashMap<>();
		try {
			
			List<WorkinghourDTO> WorkinghourList = responseDTO.getWorkinghourList();
			log.info(WorkinghourList.toString());
			String workinghour_id = responseDTO.getWorkinghour_id();
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
	public String insert_COMMUTE(@RequestBody String entity) {
		// 사원이 직접 출근한다면 시큐리티에서 사원코드를 뽑아오면 됨
		// 관리자라면 프론트에서 사원의 사원코드를 받아와야함
		String id = SecurityContextHolder.getContext().getAuthentication().getName();
		EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String employee_cd = employeeDetails.getEmployee_cd();
		String workinghour_id = employeeDetails.getWorkinghour_id();
		
		// 파라미터로 출근인지 퇴근인지 정하기
		
//		Commute commute = commuteService.findById(employeeDetails);
		Commute commute = commuteService.findById(employee_cd, workinghour_id);
		if(commute != null) { // 업데이트
			System.out.println("업데이트입니다!!!!!!!!!!!!!!");
//			int updateCount = commuteService.insert_COMMUTE(employeeDetails, commute);		
			int updateCount = commuteService.insert_COMMUTE(employee_cd, workinghour_id, commute);		
			
			if(updateCount > 0) {
				// 사원근무기록 등록
				commuteService.insert_COMHISTORY(employeeDetails.getEmployee_cd(), commute.getCommute_wt(), commute.getCommute_lt(), commute.getWorkinghour_id());
			}
			
		} else { // 인서트
			System.out.println("인서트입니다!!!!!!!!!!!!!!");
			int insertCount = commuteService.insert_COMMUTE(employeeDetails, commute);			
		}
		
		
		
		return "";
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
