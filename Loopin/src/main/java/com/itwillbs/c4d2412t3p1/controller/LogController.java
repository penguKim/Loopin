package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.LogDTO;
import com.itwillbs.c4d2412t3p1.service.LogService;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.LogFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class LogController {
	
	
	private final LogService logService;
	
	@PreAuthorize("hasRole('ROLE_SYS_ADMIN')")
	@GetMapping("/log_list")
	public String log_list() {
		
		
		
		return "/log/log_list";
	}
	
	@GetMapping("/select_LOG")
	public ResponseEntity<List<LogDTO>> select_LOG() {
        try {
            List<LogDTO> logList = logService.select_LOG(); // 로그 데이터 조회
//            log.info("logList@@ : " + logList.toString());
            return ResponseEntity.ok(logList); // JSON 형태로 반환
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	@PostMapping("/select_FILTERED_LOG")
    public ResponseEntity<List<LogDTO>> filterLogs(@RequestBody LogFilterRequest filterRequest) {
        try {
            // 필터 조건이 비어 있으면 전체 로그 반환
            if (filterRequest.isEmpty()) {
                List<LogDTO> logList = logService.select_LOG();
                return ResponseEntity.ok(logList);
            }
            
            // 필터 조건에 따른 필터링된 로그 반환
            List<LogDTO> filteredLogList = logService.select_FILTERED_LOG(filterRequest);
            
            return ResponseEntity.ok(filteredLogList);

        } catch (Exception e) {
        	e.printStackTrace();
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

	
}

