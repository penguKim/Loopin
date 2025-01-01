package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class HRAssignmentController {
	
	@GetMapping("/assignment")
	public String assignment() {
		
		return "/hr/assignment";
	}
}
