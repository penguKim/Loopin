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
		
		// 부서코드 가져오기 
		model.addAttribute("dept_list", transferService.selectDeptList("DEPARTMENT"));

		// 직위코드 가져오기 
		model.addAttribute("grade_list", transferService.selectGradeList("POSITION"));
		
		return "/hr/transfer";
	}

//	인사발령 조회
	@LogActivity(value = "조회", action = "인사발령")
	@GetMapping("/select_TRANSFER")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_TRANSFER() {
		List<Transfer> transfers = transferService.findAll();

		List<Map<String, Object>> response = transfers.stream().map(transfer -> {
			Map<String, Object> row = new HashMap<>();
			
			row.put("transfer_id", transfer.getTransfer_id());
			row.put("employee_cd", transfer.getEmployee_cd());
			row.put("transfer_ad", transfer.getTransfer_ad());
			row.put("transfer_ac", transfer.getTransfer_ac());
			row.put("transfer_og", transfer.getTransfer_og());
			row.put("transfer_ag", transfer.getTransfer_ag());
			row.put("transfer_od", transfer.getTransfer_od());
			row.put("transfer_adp", transfer.getTransfer_adp());
			return row;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

//	인사발령 등록
	@LogActivity(value = "등록", action = "인사발령")
	@PostMapping("/insert_TRANSFER")
	public ResponseEntity<Map<String, String>> insert_TRANSFER(@RequestBody TransferDTO transferDTO) {
		Map<String, String> response = new HashMap<>();
		try {
			transferService.insert_TRANSFER(transferDTO);
			response.put("message", "데이터가 성공적으로 저장되었습니다.");
			return ResponseEntity.ok(response); // JSON 형식으로 반환
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
