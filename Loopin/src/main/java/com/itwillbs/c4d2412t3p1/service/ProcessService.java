package com.itwillbs.c4d2412t3p1.service;

import java.security.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.BomallDTO;
import com.itwillbs.c4d2412t3p1.mapper.ProcessMapper;
import com.itwillbs.c4d2412t3p1.repository.ProcessRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Log
@Service
@RequiredArgsConstructor
public class ProcessService {
	
	private final ProcessMapper pcM;
	private final ProcessRepository pcR;
	
	public List<Map<String, Object>> selectpclist() {
		
		List<Map<String, Object>> list = pcM.selectpclist();
		
		return list;
	}

	public List<Map<String, Object>> selectpdgclist(String COMMON_GC) {
		List<Map<String, Object>> list = pcM.selectpdgclist(COMMON_GC);
		return list;
	}

	public List<Map<String, Object>> selectpdcclist(String pdcc, String pdgc) {

		System.out.println("받아오니? pdcc "+pdcc+"pdgc  :"+pdgc);
		List<Map<String, Object>> list = pcM.selectpdcclist(pdcc,pdgc);
		return list;
	}

	public List<Map<String, Object>> selecteqlist(String pd) {
		List<Map<String, Object>> list = pcM.selecteqlist(pd);
		return list;
	}

	public int postProcess(List<Map<String, Object>> regidata) {
		for (Map<String, Object> item : regidata) {
	        item.put("process_wd", String.valueOf(System.currentTimeMillis()));
	        if(item.get("process_eq")==null) {
	        	item.put("process_eq","");
	        }
	    }
		System.out.println("얘먼저"+regidata.get(0));
		int list = pcM.postProcess(regidata);
		return list;
	}

	public int checkpccd(String cdvalue) {
		System.out.println("받아오니?"+cdvalue);
		String result = pcM.checkpccd(cdvalue);
		System.out.println("뭐받아오는데"+result);
		if(result != null) {
			return 0;
		}else {
			return 1;
		}
	}

	public List<Map<String, Object>> selectpc(String pccd) {
		List<Map<String, Object>> list = pcM.selectpc(pccd);
		return list;
	}

	public List<Map<String, Object>> filter(List<com.itwillbs.c4d2412t3p1.entity.Process> value) {
		
		List<Map<String, Object>> list = pcM.filter(value);
		
		System.out.println("결과 가져와봐"+list);
		
		return list;
	}


}
