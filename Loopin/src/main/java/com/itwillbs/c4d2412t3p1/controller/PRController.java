package com.itwillbs.c4d2412t3p1.controller;
import java.util.List;

import javax.script.ScriptException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.PRDTO;
import com.itwillbs.c4d2412t3p1.domain.PR_calculationMDTO;
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
		log.info("가져온 값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+list.toString());
		return list;
	}

}