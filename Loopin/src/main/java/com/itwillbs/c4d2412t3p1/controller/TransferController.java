package com.itwillbs.c4d2412t3p1.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.TransferDTO;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.EmployeeService;
import com.itwillbs.c4d2412t3p1.service.TransferService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class TransferController {

	private final TransferService transferService;

	@GetMapping("/transfer_list")
	public String transfer_list(Model model) {

		model.addAttribute("dept_list", transferService.selectCommonList("DEPARTMENT"));

		model.addAttribute("grade_list", transferService.selectCommonList("POSITION"));

		model.addAttribute("TRType_list", transferService.selectCommonList("TRTYPE"));

		model.addAttribute("DPType_list", transferService.selectCommonList("DPTYPE"));

		return "/hr/transfer";
	}

//	인사발령 조회
	@GetMapping("/select_TRANSFER")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_TRANSFER() {

		try {

			EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			String role = employeeDetails.getEmployee_rl();
			String employee_cd = employeeDetails.getEmployee_cd();

			// 서비스 호출 후 결과 반환
			List<Map<String, Object>> response = transferService.select_TRANSFER_DETAIL(role, employee_cd);
			log.info("@@@@@@@@@@@@@@@@" + response.toString());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
		}

	}

	@GetMapping("/select_EMPLOYEE_COMMON")
	@ResponseBody
	public List<Map<String, Object>> select_EMPLOYEE_COMMON() {
		// Service를 호출하여 데이터 조회
		return transferService.select_EMPLOYEE_COMMON();
	}

	// 부서장 체크
	@PostMapping("/select_department_manager")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> select_DEPARTMENT_MANAGER(@RequestBody Map<String, String> request) {
		String transfer_adp = request.get("transfer_adp");
		log.info("발령 부서 체크: " + transfer_adp);

		Map<String, Object> response = new HashMap<>();
		try {
			Map<String, Object> managerInfo = transferService.select_DEPARTMENT_MANAGER(transfer_adp);

			if (managerInfo != null && !managerInfo.isEmpty()) {
				response.put("manager_exists", true);
				response.put("manager_info", managerInfo);
			} else {
				response.put("manager_exists", false);
			}

			return ResponseEntity.ok(response);

		} catch (Exception e) {
			log.severe("부서장 체크 중 오류 발생: " + e.getMessage());
			response.put("error", "부서장 체크 중 오류 발생: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}


//	인사발령 등록
	@LogActivity(value = "등록", action = "인사발령")
	@PostMapping("/insert_TRANSFER")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> insert_TRANSFER(@RequestBody TransferDTO transferDTO) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // 서비스 호출
	        Map<String, Object> updatedData = transferService.handleTransferInsert(transferDTO);

	        response.put("message", "데이터가 성공적으로 저장되었습니다.");
	        response.put("data", updatedData);

	        log.info("인사발령 등록 성공 - 응답 데이터: " + updatedData);

	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        log.severe("인사발령 등록 실패 - 에러 메시지: " + e.getMessage());
	        response.put("message", "데이터 저장 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}


	
	@LogActivity(value = "수정", action = "인사발령")
	@PutMapping("/update_TRANSFER")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> update_TRANSFER(@RequestBody TransferDTO transferDTO) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // 서비스 호출 후 변경된 데이터 반환
	        Map<String, Object> updatedData = transferService.handleTransferUpdate(transferDTO);

	        response.put("message", "데이터가 성공적으로 수정되었습니다.");
	        response.put("data", updatedData);

	        // 로깅
	        log.info("인사발령 수정 성공 - 응답 데이터: " + updatedData);

	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        log.severe("인사발령 수정 실패 - 에러 메시지: " + e.getMessage());
	        response.put("message", "데이터 수정 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

	
//	인사발령 삭제
	@LogActivity(value = "삭제", action = "인사발령")
	@PostMapping("/delete_TRANSFER")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> delete_TRANSFER(@RequestBody Map<String, List<Long>> request) {
	    Map<String, Object> response = new HashMap<>();

	    try {
	        // 요청 데이터에서 IDs 추출
	        List<Long> ids = request.get("ids");
	        if (ids == null || ids.isEmpty()) {
	            throw new IllegalArgumentException("삭제할 ID가 없습니다.");
	        }

	        // 서비스 호출
	        transferService.delete_TRANSFER(ids);

	        // 응답 데이터 구성
	        response.put("success", true);
	        response.put("deleted_ids", ids);
	        response.put("message", "데이터가 성공적으로 삭제되었습니다.");

	        // 로깅
	        log.info("인사발령 삭제 성공 - 삭제된 IDs: " + ids);

	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        log.severe("인사발령 삭제 실패 - 에러 메시지: " + e.getMessage());
	        response.put("success", false);
	        response.put("message", "데이터 삭제 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}


}