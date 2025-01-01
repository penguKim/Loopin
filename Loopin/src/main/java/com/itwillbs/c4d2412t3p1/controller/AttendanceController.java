package com.itwillbs.c4d2412t3p1.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.service.AttendanceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class AttendanceController {
	
	private final AttendanceService attendanceService;
	
	@GetMapping("attendance_list")
	public String attendance_list() {
		
		return "attendance/attendance_list";
	}
	
	@ResponseBody
	@GetMapping("/select_base_ATTENDANCE")
	public List<Map<String, Object>> select_base_ATTENDANCE() {
		log.info("들어왔냐");
		List<Map<String, Object>> list = attendanceService.select_base_ATTENDANCE();
		
		System.out.println(list);
//		{AVAILABLE=6, TOTAL=16, HIRE=20141129, USE=1, PERIOD=6, DEPT=인사팀, NAME=남예준},
		
	    return list;
	}
	
}
