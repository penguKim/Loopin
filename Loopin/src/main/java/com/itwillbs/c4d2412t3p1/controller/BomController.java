package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.itwillbs.c4d2412t3p1.domain.BomallDTO;
import com.itwillbs.c4d2412t3p1.entity.Bom;
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
	
	@PostMapping("/insertbom")
	public ResponseEntity<List<BomallDTO>> insertbom(@RequestBody Map<String, List<BomallDTO>> bomdata) {
		
		List<BomallDTO> bom = bomdata.get("bom");
		List<BomallDTO> bompc = bomdata.get("bompro");
		
		System.out.println("bom받아오니? : "+ bom.get(0).getBom_am());
		System.out.println("bom받아오니? : "+ bom.get(0).getProduct_cd());
		System.out.println("bom받아오니? : "+ bom.get(0).getBom_cd());
		System.out.println("bompc받아오니? : "+ bompc.get(0).getBomprocess_cd());
		System.out.println("bompc받아오니? : "+ bompc.get(0).getBomprocess_ap());
		System.out.println("bompc받아오니? : "+ bompc.get(0).getBomprocess_er());
		System.out.println("bompc받아오니? : "+ bompc.get(0).getBomprocess_rt());
		System.out.println("bompc받아오니? : "+ bompc.get(0).getBomprocess_ra());
		System.out.println("bompc받아오니? : "+ bompc.get(0).getBomprocess_bg());
		
		List<BomallDTO> list = bS.insertbom(bom, bompc);
		
		return ResponseEntity.ok(null);
	}
	
	
}
