package com.itwillbs.c4d2412t3p1.controller;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class BomController {

	@GetMapping("/bom")
	public String BOM() {
		return "bom/bom";
	}
	
	
}
