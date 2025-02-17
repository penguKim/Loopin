package com.itwillbs.c4d2412t3p1.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itwillbs.c4d2412t3p1.service.ProcessService;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;


@Log
@Controller
@RequiredArgsConstructor
public class ProcessController {

	private final ProcessService pcS;
	
	@GetMapping("/process")
	public String process () {
		return "process/process";
	}
	
	@GetMapping("/selectpclist")
	@ResponseBody
	public List<Map<String,Object>> selectpclist () {
		
		List<Map<String,Object>> list = pcS.selectpclist(); 
		
		return list;
	}
	
	@GetMapping("/selectpdgclist")
	@ResponseBody
	public List<Map<String,Object>> selectpdgclist (@RequestParam("COMMOM_GC") String COMMON_GC) {
		
		List<Map<String,Object>> list = pcS.selectpdgclist(COMMON_GC); 
		
		return list;
	}

	@GetMapping("/selectpdcclist")
	@ResponseBody
	public List<Map<String,Object>> selectpdcclist (@RequestParam("pdcc") String pdcc, @RequestParam("pdgc") String pdgc) {
		
		List<Map<String,Object>> list = pcS.selectpdcclist(pdcc, pdgc); 
		
		return list;
	}
	
	@GetMapping("/selecteqlist")
	@ResponseBody
	public List<Map<String,Object>> selecteqlist (@RequestParam("pd") String pd) {
		
		System.out.println("&로 넘어오지?"+ pd);
		
		List<Map<String,Object>> list = pcS.selecteqlist(pd); 
		
		return list;
	}
	
	@PostMapping("postProcess")
	@ResponseBody
	public int postProcess(@RequestBody List<Map<String,Object>> regidata) {
		
		System.out.println("말해바"+ regidata.get(0).get("process_cd"));
		System.out.println("말해바"+ regidata.get(0).get("process_nm"));
		System.out.println("말해바"+ regidata.get(0).get("process_gc"));
		System.out.println("말해바"+ regidata.get(0).get("process_cc"));
		System.out.println("말해바"+ regidata.get(0).get("process_pd"));
		System.out.println("말해바"+ regidata.get(0).get("process_eq"));
		System.out.println("말해바"+ regidata.get(0).get("process_wr"));
		System.out.println("말해바"+ regidata.get(0).get("process_bg"));
		
		int result = pcS.postProcess(regidata);
		
		System.out.println("저장되니"+result);
		
		return result;
	}
	
	@GetMapping("/checkpccd")
	@ResponseBody
	public int checkpccd(@RequestParam("cdvalue") String cdvalue) {
		System.out.println("받아오냐?"+cdvalue);
		int result = pcS.checkpccd(cdvalue);
		System.out.println("서비스에서 받아오냐?"+result);
		
		return result;
	}
	
	@GetMapping("/selectpc")
	@ResponseBody
	public List<Map<String,Object>> selectpc (@RequestParam("pccd") String pccd) {
		
		List<Map<String,Object>> list = pcS.selectpc(pccd); 
		
		return list;
	}
	
}
