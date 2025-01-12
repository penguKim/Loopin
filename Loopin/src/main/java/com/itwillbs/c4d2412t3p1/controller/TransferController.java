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
import org.springframework.web.bind.annotation.RestController;

import com.itwillbs.c4d2412t3p1.domain.TransferDTO;
import com.itwillbs.c4d2412t3p1.entity.Transfer;
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
	private final EmployeeService employeeService;

	@GetMapping("/transfer_list")
	public String transfer_list(Model model) {

		model.addAttribute("dept_list", transferService.selectDeptList("DEPARTMENT"));

		model.addAttribute("grade_list", transferService.selectGradeList("POSITION"));

		model.addAttribute("TRType_list", transferService.selectTRTypeList("TRTYPE"));

		model.addAttribute("DPType_list", transferService.selectTRTypeList("DPTYPE"));

		return "/hr/transfer";
	}

//	인사발령 조회
	@LogActivity(value = "조회", action = "인사발령")
	@GetMapping("/select_TRANSFER")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_TRANSFER() {

		try {
			// 서비스 호출 후 결과 반환
			List<Map<String, Object>> response = transferService.select_TRANSFER_DETAIL();
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

			if (managerInfo != null) {
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
	public ResponseEntity<Map<String, String>> insert_TRANSFER(@RequestBody TransferDTO transferDTO) {
		Map<String, String> response = new HashMap<>();
		try {
	        // 서비스 계층에 작업 위임
	        transferService.handleTransferInsert(transferDTO);

	        response.put("message", "데이터가 성공적으로 저장되었습니다.");
	        return ResponseEntity.ok(response);
	    } catch (IllegalArgumentException e) {
	        response.put("message", "데이터 저장 실패: " + e.getMessage());
	        return ResponseEntity.badRequest().body(response);
	    } catch (Exception e) {
	        response.put("message", "데이터 저장 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}

//	인사발령 삭제
	@LogActivity(value = "삭제", action = "인사발령")
	@PostMapping("/delete_TRANSFER")
	public ResponseEntity<Map<String, Object>> delete_TRANSFER(@RequestBody Map<String, List<Long>> request) {
		List<Long> ids = request.get("ids");
		log.info("삭제 요청 데이터: " + request.toString());

		log.info(ids.toString());
		Map<String, Object> response = new HashMap<>();

		try {
			transferService.delete_TRANSFER(ids); // Service 계층에서 삭제 처리
			response.put("success", true);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

}
