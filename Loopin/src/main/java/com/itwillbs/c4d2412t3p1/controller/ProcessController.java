package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.service.ProcessService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@Controller
@RequiredArgsConstructor
public class ProcessController {

	private final ProcessService pcS;
	
	@GetMapping("/process")
	public String process () {
		return "process/process";
	}
	
	@GetMapping("/selectpclist")
	@ResponseBody
	public List<Map<String,Object>> selectpclist () {
		
		List<Map<String,Object>> list = pcS.selectpclist(); 
		
		return list;
	}
	
	
}
