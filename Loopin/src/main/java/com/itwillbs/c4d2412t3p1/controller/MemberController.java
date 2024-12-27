package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itwillbs.c4d2412t3p1.domain.MemberDTO;
import com.itwillbs.c4d2412t3p1.service.MemberRepService;
import com.itwillbs.c4d2412t3p1.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class MemberController {

	private final MemberService memberService;
	private final MemberRepService memberRepService;
	
	@GetMapping("/mapper")
	public String mapperTest() {
		
		List<Map<String, Object>> memberList = memberService.memberList();
		log.info(memberList.toString());
		
		return "redirect:/main";
	}
	
	@GetMapping("/main")
	public String main() {
		
		log.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
		
		String id = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info(id);
		
		return "/member/main";
	}
	
	@GetMapping("/login")
	public String login() {
		return "/member/login";
	}
	
	@GetMapping("/insert")
	public String insert() {
		log.info("get insert");
		return "/member/insert";
	}
	
	@PostMapping("/insert")
	public String insertPro(MemberDTO memberDTO) {
		log.info("post insert");
		log.info(memberDTO.toString());

		memberRepService.save(memberDTO);
		
		return "redirect:/login";
	}
	
	
	
}
