package com.itwillbs.c4d2412t3p1.controller;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.service.ApprovalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class ApprovalController {

	private final ApprovalService approvalService;

	// 결재 페이지로 이동
	@GetMapping("/approval_list")
	public String approval_list(Model model) {

		EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		String role = employeeDetails.getEmployee_rl();

		// 기안서 구분 가져오기
		model.addAttribute("DRAFT_list", approvalService.selectCommonList("DRAFT"));

		model.addAttribute("ANNUAL_list", approvalService.selectCommonList("ANNUAL"));

		// 롤값 가져오기
		model.addAttribute("role", role);

		return "/approval/approval_list";
	}

	@GetMapping("/approval_data")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> getApprovalData(@RequestParam("tabType") String tabType) {
		try {
			List<Map<String, Object>> response = approvalService.getApprovalsForTab(tabType);
			log.info("%%%%%%%%%" + response.toString());
			return ResponseEntity.ok(response);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}

	@PostMapping("/insert_APPROVAL")
	public ResponseEntity<Map<String, String>> insert_APPROVAL(@RequestPart("ApprovalDTO") ApprovalDTO approvalDTO) {
		Map<String, String> response = new HashMap<>();

		try {
			// 데이터 저장 처리
			log.info("approvalDTO : " +  approvalDTO);
			approvalService.handleApprovalInsert(approvalDTO);

			response.put("message", "데이터가 성공적으로 저장되었습니다.");
			return ResponseEntity.ok(response); // JSON 형식으로 반환
		} catch (Exception e) {
			response.put("message", "데이터 저장 실패: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/get_employee_details")
	public ResponseEntity<Map<String, Object>> getEmployeeDetails(
			@RequestParam("firstApproverCd") String firstApproverCd,
			@RequestParam("secondApproverCd") String secondApproverCd) {

		Map<String, Object> response = new HashMap<>();

		try {
			// Service에서 결재권자 정보 가져오기
			Employee firstApprover = approvalService.getEmployeeByCd(firstApproverCd);
			Employee secondApprover = approvalService.getEmployeeByCd(secondApproverCd);

			response.put("firstApprover", firstApprover);
			response.put("secondApprover", secondApprover);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("error", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@PostMapping("/update_approval_process")
	public ResponseEntity<String> update_approval_process(@RequestBody Map<String, String> request) {
		EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();

		String currentUserCd = employeeDetails.getEmployee_cd();
		String approvalCd = request.get("approvalCd");
		String actionType = request.get("actionType");

		if (approvalCd == null || actionType == null || currentUserCd == null) {
			return ResponseEntity.badRequest().body("결재 코드, 동작 유형 및 사용자 ID는 필수입니다.");
		}

		try {
			if ("approve".equalsIgnoreCase(actionType)) {
				approvalService.processApproval(approvalCd, currentUserCd, true); // 승인 처리
			} else if ("reject".equalsIgnoreCase(actionType)) {
				approvalService.processApproval(approvalCd, currentUserCd, false); // 반려 처리
			} else {
				return ResponseEntity.badRequest().body("올바르지 않은 동작 유형입니다.");
			}

			return ResponseEntity.ok("결재가 " + ("approve".equalsIgnoreCase(actionType) ? "승인" : "반려") + "되었습니다.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생: " + e.getMessage());
		}
	}

//	@PostMapping("/update_APPROVAL")
//	public ResponseEntity<Map<String, String>> update_APPROVAL(@RequestPart("ApprovalDTO") ApprovalDTO approvalDTO// DTO
//																													// 받기
//	) {
//
//		Map<String, String> response = new HashMap<>();
//
//		String employee_id = SecurityContextHolder.getContext().getAuthentication().getName();
//
//		try {
//
//			String approval_cd = approvalDTO.getApproval_cd();
//
//			if (approval_cd == null) {
//				response.put("message", "데이터 수정 실패: ID(approval_cd)가 전달되지 않았습니다.");
//				return ResponseEntity.badRequest().body(response);
//			}
//
//			// 기존 데이터 조회
//			Approval approval = approvalService.findApprovalsByEmployee(approvalDTO.getApproval_cd());
//			if (approval == null) {
//				response.put("message", "데이터 수정 실패: 해당 ID의 데이터를 찾을 수 없습니다.");
//				return ResponseEntity.badRequest().body(response);
//			}
//
//			// 수정자 처리
//			approvalDTO.setApproval_mf(employee_id);
//
//			// 수정일 처리
//			approvalDTO.setApproval_md(new Timestamp(System.currentTimeMillis()));
//
//			// Service 호출
//			approvalService.update_APPROVAL(approvalDTO);
//
//			// 성공 응답
//			response.put("message", "데이터가 성공적으로 수정되었습니다.");
//			return ResponseEntity.ok(response);
//
//		} catch (IllegalArgumentException e) {
//			// ID로 조회되지 않는 경우 처리
//			response.put("message", "데이터 수정 실패: " + e.getMessage());
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//
//		} catch (Exception e) {
//			// 기타 예외 처리
//			response.put("message", "데이터 수정 실패: 알 수 없는 오류가 발생했습니다. " + e.getMessage());
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
//		}
//	}

//	결재 삭제
	@PostMapping("/delete_APPROVAL")
	public ResponseEntity<Map<String, Object>> delete_APPROVAL(@RequestBody Map<String, List<String>> request) {

		List<String> cds = request.get("approval_cds");

		log.info("삭제 요청 데이터: " + request.toString());

		Map<String, Object> response = new HashMap<>();

		try {
			approvalService.delete_APPROVAL(cds); // Service 계층에서 삭제 처리
			response.put("success", true);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}

	@GetMapping("/first_approvers")
	public ResponseEntity<Map<String, Object>> getFirstApprovers(
			@AuthenticationPrincipal EmployeeDetails employeeDetails) {
		String employee_gd = employeeDetails.getEmployee_gd();
		log.info("현재 사원코드 : " + employee_gd);
		List<Map<String, Object>> firstApprovers = approvalService.getFirstApproverListWithDeptAndPosition(employee_gd);
		log.info("현재 1차결재권자리스트 : " + firstApprovers.toString());

		// Grid에서 요구하는 형식으로 응답 변환
		Map<String, Object> response = new HashMap<>();
		response.put("result", true);
		response.put("data", Map.of("contents", firstApprovers));
		return ResponseEntity.ok(response);
	}

	@GetMapping("/second_approvers")
	public ResponseEntity<Map<String, Object>> getSecondApprovers(@RequestParam("approval_fa") String approval_fa) {
		List<Map<String, Object>> secondApprovers = approvalService
				.getSecondApproverListWithDeptAndPosition(approval_fa);
		log.info("2차 결재권자리스트 : " + secondApprovers);

		// Grid에서 요구하는 형식으로 응답 변환
		Map<String, Object> response = new HashMap<>();
		response.put("result", true);
		response.put("data", Map.of("contents", secondApprovers));

		return ResponseEntity.ok(response);
	}

//    // 필터 데이터 가져오기
//	@PostMapping("/select_FILTERED_APPROVAL")
//    public ResponseEntity<List<Approval>> select_FILTERED_APPROVAL(@RequestBody APPROVALFilterRequest filterRequest) {
//		log.info("@@@@@@@@@@@@@@@@@");
//		
//		System.out.println("@@@@@@@@@" + filterRequest);
//		System.out.println("@@@@@@@@@" + filterRequest.getStartDate());
//		System.out.println("@@@@@@@@@" + filterRequest.getEndDate());
//		
//        try {
//            // 필터 조건이 비어 있으면 전체 인사정보 반환
//            if (filterRequest.isEmpty()) {
//            	List<Approval> approvals = approvalService.findAll();
//                return ResponseEntity.ok(approvals);
//            }
//            
//            log.info(filterRequest.toString()); // 전체 필드 출력
//            log.info(filterRequest.getStartDate()); // 시작일
//            log.info(filterRequest.getEndDate()); // 종료일
//            
//            
//            
//            
//            // 필터 조건에 따른 필터링된 인사정보 반환
//            List<Approval> filteredEmployeeList = approvalService.select_FILTERED_APPROVAL(filterRequest);
//            
//            System.out.println("@@@@@@@@" + filteredEmployeeList);
//            
//            
//            
//            return ResponseEntity.ok(filteredEmployeeList);
//
//        } catch (Exception e) {
//        	e.printStackTrace();
//            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

}
