package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanDTO;
import com.itwillbs.c4d2412t3p1.service.ProductplanService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Log
@Controller
public class ProductPlanController {
	
	private final ProductplanService productplanService;
	
	@GetMapping("/product_plan_list")
	public String product_plan() {
		
		return "productplan/productplan_list";
	}
	
	@GetMapping("/select_CONTRACTCD_list")
	@ResponseBody
    public ResponseEntity<List<ContractDetailDTO>> getContractDetails(@RequestParam(value = "contract_cd") String contract_cd) {
        List<ContractDetailDTO> contractDetails = productplanService.select_CONTRACTCD_list(contract_cd);
        log.info("수주바디 내용!!" + contractDetails.toString());
        return ResponseEntity.ok(contractDetails);
    }
	
}
