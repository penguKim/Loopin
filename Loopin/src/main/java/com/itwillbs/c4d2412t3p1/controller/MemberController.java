package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.itwillbs.c4d2412t3p1.domain.MemberDTO;
import com.itwillbs.c4d2412t3p1.service.MemberRepService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class MemberController {

	private final MemberRepService memberRepService;
	
	@GetMapping("/main")
	public String main() {
		
		log.info(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
		
		String id = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info(id);
		
		return "/main";
	}
	
	@GetMapping("/login")
	public String login() {
		return "/login";
	}

	@GetMapping("/logout")
	public String logout() {
		return "redirect:/login";
	}
	
	@GetMapping("/insert")
	public String insert() {
		log.info("get insert");
		return "/insert";
	}
	
	@PostMapping("/insert")
	public String insertPro(MemberDTO memberDTO) {
		log.info("post insert");
		log.info(memberDTO.toString());

		memberRepService.save(memberDTO);
		
		return "redirect:/login";
	}
	
	
	
}
