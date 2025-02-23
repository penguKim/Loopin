package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.c4d2412t3p1.domain.BomallDTO;
import com.itwillbs.c4d2412t3p1.entity.BomProcess;
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
	
	@GetMapping("/selectBomAll")
	public ResponseEntity<List<BomProcess>> selectBomAll() {
		
		List<BomProcess> list = bS.selectBomAll();
		
		return ResponseEntity.ok(list);
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
	
	@GetMapping("/selectbom")
	public ResponseEntity<List<Product>> selectbom(@RequestParam("pdc") List<String> ckrowpds) {
		
		List<Product> list = bS.selectbom(ckrowpds);
		
		return ResponseEntity.ok(list);
	}
	
	@SuppressWarnings("unchecked")
	@PostMapping("/insertbom")
	public ResponseEntity<Integer> insertbom(@RequestBody Map<String, List<BomallDTO>> bomdata) {
		
		try {
		List<BomallDTO> bom = bomdata.get("bom");
		List<BomallDTO> bompc = bomdata.get("bompro");
		
		System.out.println("bom받아오니? : "+ bom.get(0).getBom_am());
		System.out.println("bom받아오니? : "+ bom.get(0).getProduct_cd());
		System.out.println("bom받아오니? : "+ bom.get(0).getBom_cd());
		System.out.println("bompc받아오니? : "+ bompc.get(0).getBomprocess_cd());
		System.out.println("bompc받아오니? : "+ bompc.get(0).getBomprocess_ap());
		System.out.println("bompc받아오니? : "+ bompc.get(0).getBomprocess_er());
		
		bS.insertbom(bom, bompc);
		
		return ResponseEntity.ok(0);
		} catch (RuntimeException e) {
			return (ResponseEntity<Integer>) ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/selectpcbom")
	public ResponseEntity<Map<String, Object>> selectpcbom(@RequestParam("bpap") String bpap, @RequestParam("pdcd") String pdcd,@RequestParam("bpcd") String bpcd,@RequestParam("bppc") String bppc) {
		
		System.out.println("너 먼데"+bpap);
		System.out.println("너 먼데"+pdcd);
		System.out.println("너 먼데"+bpcd);
		
        Map<String, Object> list = bS.selectbom(bpap, pdcd, bpcd, bppc); 
        
        return ResponseEntity.ok(list);
	}
	
	@PostMapping("/deletebom")
	public ResponseEntity<Integer> deletebom(@RequestBody List<BomallDTO> deletedata) {
		
		System.out.println("받아오니...?: "+deletedata.get(0).getBomproduct_cd());
		
		int list = bS.deletebom(deletedata);
		
		return ResponseEntity.ok(list);
	}
	
}
