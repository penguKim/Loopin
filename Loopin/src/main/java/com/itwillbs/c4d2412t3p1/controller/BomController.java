package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.itwillbs.c4d2412t3p1.entity.Product;
import com.itwillbs.c4d2412t3p1.service.BomService;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor
public class BomController {

	private final BomService bS;
	
	@GetMapping("/bom")
	public String BOM() {
		return "bom/bom";
	}
	
	@GetMapping("/selpd")
	public ResponseEntity<List<Product>> getMethodName() {
		
		List<Product> list = bS.selectPD();
		
		return ResponseEntity.ok(list);
	}
	
}
