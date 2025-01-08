package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.entity.PRCode;
import com.itwillbs.c4d2412t3p1.mapper.PRMapper;
import com.itwillbs.c4d2412t3p1.repository.PRCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PRService {

	private final PRCodeRepository prcRep;
	
	public List<PRCode> getprcode() {

		List<PRCode> list = prcRep.findAll();
		
		return list;
	}




	private final PRMapper prM;
	
	public List<Map<String, Object>> selectpradmin() {

		List<Map<String, Object>> list = prM.selectpradmin();
		
		return list;
	}

	public List<Map<String, Object>> selectpradminfirstmodal(Long pr_id) {

		List<Map<String, Object>> list = prM.selectpradminfirstmodal(pr_id);
		
		return list;
	}

	public List<Map<String, Object>> selectpradminfirstmodal2(Long prdetail_id) {
		
		List<Map<String, Object>> list = prM.selectpradminfirstmodal2(prdetail_id);
		
		return list;
	}

	
}
