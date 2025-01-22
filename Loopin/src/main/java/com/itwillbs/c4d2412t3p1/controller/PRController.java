package com.itwillbs.c4d2412t3p1.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.catalina.connector.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.domain.PRDTO;
import com.itwillbs.c4d2412t3p1.domain.PR_calculationMDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.PRCode;
import com.itwillbs.c4d2412t3p1.service.PRService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Controller
@RequiredArgsConstructor
public class PRController {

	private final PRService prS;
	
	@GetMapping("/check_salary")
	public String checkSalary() {
		return "payroll/checkSalary";
	}
	
	@GetMapping("/checkpr")
	@ResponseBody
	public List<Map<String,Object>> checkpr(@RequestParam("employee_cd") String employee_cd) {
		
		List<Map<String, Object>> list = prS.selectpr(employee_cd);
		
		return list;
	}
	
	@GetMapping("/checkprmodal")
	@ResponseBody
	public List<Map<String,Object>> checkprmodal(@RequestParam("employee_cd") String employee_cd, @RequestParam("pr_id") Long pr_id) {
		
		List<Map<String, Object>> list = prS.selectprmodal(pr_id, employee_cd);
		
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
	
	@GetMapping("/check_pradmin")
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
		String employee_cd = data.getEmployee_cd();
		String employee_nm = data.getEmployee_nm();
		String workingtime = data.getWorkingtime();
		String overworkingtime = data.getOverworkingtime();
		String nightworkingtime = data.getNightworkingtime();
		String weekendworkingtime = data.getWeekendworkingtime();
		String holydayworkingtime = data.getHolydayworkingtime();
		String remainleave = data.getRemainleave();
		String bonus = data.getBonus();
		List<PRDTO> list = prS.calculatingMachine(employee_cd, employee_nm, BS,workingtime,overworkingtime,nightworkingtime,weekendworkingtime,holydayworkingtime,remainleave,bonus);
		log.info("가져온 값!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+list.toString());
		return list;
	}

	@GetMapping("/prsend")
	public String prsend() {
		return "payroll/sendPR";
	}

	@GetMapping("/getempandbs")
	@ResponseBody
	public List<Employee> getempandbs(@RequestParam("premth") String premth) {
		
		List<Employee> list = prS.select_empworklastmth(premth);
		
		return list;
	}
	
	@GetMapping("/getworkingtimeformth")
	@ResponseBody
	public List<Map<String, Object>> getworkingtimeformth(@RequestParam("employee_cds") List<String> employee_cdList, @RequestParam("premth") String premth) {
		
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!employee_list :"+employee_cdList);
		List<Map<String, Object>> list = prS.select_worktimelastmth(employee_cdList, premth);
		
		return list;
	}
	
	@PostMapping("/update_commuteprandcalpr")
	@ResponseBody
	public List<Map<String,Object>> update_commutepr(@RequestBody List<PR_calculationMDTO> wtdata) throws ScriptException {
		prS.update_commutepr();

		List<Map<String, Object>> list = prS.calculatesalAllemp(wtdata);
		
		return list;
	}
	
	@GetMapping("/iscal")
	@ResponseBody
	public String iscal(@RequestParam("premth") String premth) {
		
		String result = prS.isCal( premth);
		return result;
	}
	
	@PostMapping("/calwbn")
	@ResponseBody
	public List<Map<String, Object>> getwt(@RequestBody List<PR_calculationMDTO> calbndata ) throws ScriptException {
		
		List<Map<String, Object>> list = prS.select_worktimeforbn(calbndata);
		
		return list;
	}
}
