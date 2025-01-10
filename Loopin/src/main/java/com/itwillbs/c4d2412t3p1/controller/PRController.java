package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.PRDTO;
import com.itwillbs.c4d2412t3p1.domain.PR_calculationMDTO;
import com.itwillbs.c4d2412t3p1.entity.PRCode;
import com.itwillbs.c4d2412t3p1.service.PRService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
public class PRController {

	private final PRService prS;
	
	@GetMapping("/prcal")
	public String prcal() {
		return "payroll/prcalcul";
	}
	
	@PostMapping("/calculate")
	@ResponseBody
	public List<PRDTO> calsal(@RequestBody PR_calculationMDTO data) throws ScriptException{
		log.info("클라이언트값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+data.toString());
		String BS = data.getBS();
		log.info("BS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+BS);
		String overworkingtime = data.getOverworkingtime();
		String nightworkingtime = data.getNightworkingtime();
		String weekendworkingtime = data.getWeekendworkingtime();
		String holydayworkingtime = data.getHolydayworkingtime();
		String leastannual = data.getLeastannual();
		String bonus = data.getBonus();
		List<PRDTO> list = prS.calculatingMachine(BS,overworkingtime,nightworkingtime,weekendworkingtime,holydayworkingtime,leastannual,bonus);
//		for(PR_calculationMDTO d : data) {
//		}
//		log.info("클라이언트값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+data.toString());
//		List<PRDTO> list = prS.calculatingMachine(data);
		log.info("가져온 값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+list.toString());
		return list;
	}
	
	@GetMapping("/prcode")
	public String prcode() {
		return "payroll/prcode";
	}
	
	@GetMapping("/getprcode")
	@ResponseBody
	public List<PRCode> getprcode() {
		
		List<PRCode> list = prS.getprcode();
		
		return list;
	}
	
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
