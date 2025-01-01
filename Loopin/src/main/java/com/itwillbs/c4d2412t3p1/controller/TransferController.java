package com.itwillbs.c4d2412t3p1.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.itwillbs.c4d2412t3p1.domain.TransferDTO;
import com.itwillbs.c4d2412t3p1.entity.Transfer;
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

		return "/hr/transfer";
	}

//	인사발령 조회
	@GetMapping("/select_TRANSFER")
	public String select_TRANSFER(Model model) {

		List<Transfer> transferList = transferService.findAll();
		log.info(transferList.toString());
		model.addAttribute("transferList", transferList);

		return "/hr/transfer";
	}

//	인사발령 등록
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

}
