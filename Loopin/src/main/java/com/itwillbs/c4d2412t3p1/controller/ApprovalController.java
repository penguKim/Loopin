package com.itwillbs.c4d2412t3p1.controller;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.service.ApprovalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;



@RequiredArgsConstructor
@Controller
@Log
public class ApprovalController {

	
	private final ApprovalService approvalService;
	
	// 결재 페이지로 이동
	@GetMapping("/approvals_list")
	public String approvals_list() {
		
		return "/approvals/approvals_list";
	}

	// 결재 현황 조회
	@GetMapping("/select_APPROVAL")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_APPROVAL() {
	    List<Approval> approvals = approvalService.findAll(); // 모든 결재 정보를 가져옵니다.

	    List<Map<String, Object>> response = approvals.stream().map(approval -> {
	        Map<String, Object> row = new HashMap<>();
	        row.put("approval_cd", approval.getApproval_cd());
	        row.put("approval_sd", approval.getApproval_sd());
	        row.put("approval_ed", approval.getApproval_ed());
	        row.put("approval_dv", approval.getApproval_dv());
	        row.put("approval_tt", approval.getApproval_tt());
	        row.put("approval_ct", approval.getApproval_ct());
	        row.put("approval_wr", approval.getApproval_wr());
	        row.put("approval_wd", approval.getApproval_wd());
	        row.put("approval_mf", approval.getApproval_mf());
	        row.put("approval_md", approval.getApproval_md());

	        return row;
	    }).collect(Collectors.toList());

	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/insert_APPROVAL")
	public ResponseEntity<Map<String, String>> insert_APPROVAL(
		    @RequestPart("ApprovalDTO") ApprovalDTO approvalDTO // DTO 받기
		    ) {
		Map<String, String> response = new HashMap<>();
		
		try {
			
			approvalDTO.setApproval_wd(new Timestamp(System.currentTimeMillis()));
	        // 데이터 저장 처리
			approvalService.insert_APPROVAL(approvalDTO);
			
			response.put("message", "데이터가 성공적으로 저장되었습니다.");
			return ResponseEntity.ok(response); // JSON 형식으로 반환
		} catch (Exception e) {
			response.put("message", "데이터 저장 실패: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
}
