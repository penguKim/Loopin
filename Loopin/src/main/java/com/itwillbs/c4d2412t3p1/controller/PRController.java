package com.itwillbs.c4d2412t3p1.controller;
import java.util.List;

import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.itwillbs.c4d2412t3p1.service.PRService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PRController {
	
	private final PRService prS;
	
	@GetMapping("/PRadmin")
	public String pradmin() {
		return "payroll/checkPRadmin";
	}
	
	@GetMapping("/checkpradmin")
	@ResponseBody
	public List<Map<String,Object>> selectpradmin() {
		
		List<Map<String, Object>> list = prS.selectpradmin();
		
		return list;
	}
	
	@GetMapping("/PRadminmadal1")
	@ResponseBody
	public List<Map<String,Object>> adminprmodal(@RequestParam("pr_id") Long pr_id) {
		
		List<Map<String, Object>> list = prS.selectpradminfirstmodal(pr_id);
		
		return list;
	}
	@GetMapping("/PRadminmadal2")
	@ResponseBody
	public List<Map<String,Object>> adminprmodal2(@RequestParam("prdetail_id") Long prdetail_id) {
		
		List<Map<String, Object>> list = prS.selectpradminfirstmodal2(prdetail_id);
		
		return list;
	}
}