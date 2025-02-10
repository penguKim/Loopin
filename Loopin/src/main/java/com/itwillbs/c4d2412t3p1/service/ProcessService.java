package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

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


}
