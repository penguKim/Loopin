package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.logging.LogActivity;
import com.itwillbs.c4d2412t3p1.service.LogService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class LogController {
	
	private final LogService logService;
	
	@GetMapping("/log_list")
	public String log_list() {
		
		return "/log/log_list";
	}
	
	@GetMapping("/select_LOG")
	public ResponseEntity<List<LogDTO>> select_LOG() {
        try {
            List<LogDTO> logList = logService.select_LOG(); // 로그 데이터 조회
            return ResponseEntity.ok(logList); // JSON 형태로 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
}
