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

}
