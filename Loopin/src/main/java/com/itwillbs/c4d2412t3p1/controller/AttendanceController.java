package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class AttendanceController {

	@GetMapping("/attendancelist")
	public String attendancelist() {
		return "/attendance/attendancelist";
	}
//	@GetMapping("/select_ATTENDANCE")
//	public String select_ATTENDANCE() {
//		return "/select_ATTENDANCE";
//	}
	
}
