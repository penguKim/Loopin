package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public ResponseEntity<List<Product>> selpd() {
		
		List<Product> list = bS.selectPD();
		
		return ResponseEntity.ok(list);
	}
	
	@GetMapping("/checkpcd")
	public ResponseEntity<Integer> checkpcd(@RequestParam("pcd") String pcd) {
		
		Integer response = bS.checkpcd(pcd);
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/selectPCs")
	public ResponseEntity<List<Process>> selectPCs() {
		
		List<Process> list = bS.selectPCs();
		
		return ResponseEntity.ok(list);
	}
	
}
