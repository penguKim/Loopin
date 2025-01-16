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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.NoticeDTO;
import com.itwillbs.c4d2412t3p1.entity.Notice;
import com.itwillbs.c4d2412t3p1.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;



@RequiredArgsConstructor
@Controller
@Log
public class NoticeController {

	
	private final NoticeService noticeService;
	
	// 결재 페이지로 이동
	@GetMapping("/notice_list")
	public String notice_list(Model model) {
		
		EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String role = employeeDetails.getEmployee_rl();

		// 롤값 가져오기 
		model.addAttribute("role", role);
		
		return "/notice/notice_list";
	}

	// 결재 현황 조회
	@GetMapping("/select_NOTICE")
	@ResponseBody
	public ResponseEntity<List<Map<String, Object>>> select_NOTICE() {
		
		EmployeeDetails employeeDetails = (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		String currentCd = employeeDetails.getEmployee_cd();
		String currentRole = employeeDetails.getEmployee_rl(); // 현재 사용자의 권한
		
		List<Notice> notices;
		
		notices = noticeService.findAll(); // 모든 공지사항 데이터 가져옵니다.
		
	    List<Map<String, Object>> response = notices.stream().map(notice -> {
	        Map<String, Object> row = new HashMap<>();
	        row.put("notice_cd", notice.getNotice_cd());
	        row.put("notice_tt", notice.getNotice_tt());
	        row.put("notice_ct", notice.getNotice_ct());
	        row.put("notice_wr", notice.getNotice_wr());
	        row.put("notice_wd", notice.getNotice_wd());
	        row.put("notice_mf", notice.getNotice_mf());
	        row.put("notice_md", notice.getNotice_md());

	        return row;
	    }).collect(Collectors.toList());

	    return ResponseEntity.ok(response);
	}
	
	@PostMapping("/insert_NOTICE")
	public ResponseEntity<Map<String, String>> insert_NOTICE(
		    @RequestPart("NoticeDTO") NoticeDTO noticeDTO // DTO 받기
		    ) {
		Map<String, String> response = new HashMap<>();
		
		// 시큐리티 세션 값 가져오기
		String employee_id = SecurityContextHolder.getContext().getAuthentication().getName(); 
		
		try {
	        // 작성자 처리
			noticeDTO.setNotice_wr(employee_id);
			
			// 작성일 처리
			noticeDTO.setNotice_wd(new Timestamp(System.currentTimeMillis()));
	        // 데이터 저장 처리
			noticeService.insert_NOTICE(noticeDTO);
			
			response.put("message", "데이터가 성공적으로 저장되었습니다.");
			return ResponseEntity.ok(response); // JSON 형식으로 반환
		} catch (Exception e) {
			response.put("message", "데이터 저장 실패: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	@PostMapping("/update_NOTICE")
	public ResponseEntity<Map<String, String>> update_NOTICE(
	        @RequestPart("NoticeDTO") NoticeDTO noticeDTO// DTO 받기
	        ) {
		
	    Map<String, String> response = new HashMap<>();

	    String employee_id = SecurityContextHolder.getContext().getAuthentication().getName();
	    
	    try {
	    	
	    	String notice_cd = noticeDTO.getNotice_cd();
	    	
	        if (notice_cd == null) {
	            response.put("message", "데이터 수정 실패: ID(notice_cd)가 전달되지 않았습니다.");
	            return ResponseEntity.badRequest().body(response);
	        }

	        
	        // 기존 데이터 조회
	        Notice notice = noticeService.findNoticeById(noticeDTO.getNotice_cd());
	        if (notice == null) {
	            response.put("message", "데이터 수정 실패: 해당 CD의 데이터를 찾을 수 없습니다.");
	            return ResponseEntity.badRequest().body(response);
	        }
	        
	        // 기존 notice_wr 값 유지 
	        noticeDTO.setNotice_wr(notice.getNotice_wr());

	        // 기존 notice_wr 값 유지 
	        noticeDTO.setNotice_wd(notice.getNotice_wd());
	        
	        // 수정자 처리
	        noticeDTO.setNotice_mf(employee_id);

	        // 수정일 처리
	        noticeDTO.setNotice_md(new Timestamp(System.currentTimeMillis()));

	        // Service 호출
	        noticeService.update_NOTICE(noticeDTO);

	        // 성공 응답
	        response.put("message", "데이터가 성공적으로 수정되었습니다.");
	        return ResponseEntity.ok(response);

	    } catch (IllegalArgumentException e) {
	        // ID로 조회되지 않는 경우 처리
	        response.put("message", "데이터 수정 실패: " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

	    } catch (Exception e) {
	        // 기타 예외 처리
	        response.put("message", "데이터 수정 실패: 알 수 없는 오류가 발생했습니다. " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}


	
	
//	공지사항 삭제
	@PostMapping("/delete_Notice")
	public ResponseEntity<Map<String, Object>> delete_Notice(@RequestBody Map<String, List<String>> request) {
		
		List<String> cds = request.get("notice_cds");
		
		System.out.println(cds + "@@@@@@@@@@@@@@@@@@@@@@@");
		
		log.info("삭제 요청 데이터: " + request.toString());
		
		Map<String, Object> response = new HashMap<>();

		try {
			noticeService.delete_NOTICE(cds); // Service 계층에서 삭제 처리
			response.put("success", true);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			response.put("success", false);
			response.put("message", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
	}
	
	
	
}
