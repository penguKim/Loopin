package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.mapper.PRMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PRService {

	private final PRMapper prM;

	public List<Map<String, Object>> selectpr(Long employee_cd) {

		List<Map<String, Object>> list = prM.selectpr(employee_cd);

		return list;
	}

	public List<Map<String, Object>> selectprmodal(Long pr_id, Long employee_cd) {

		List<Map<String, Object>> list = prM.checkprmodal(pr_id, employee_cd);

		return list;
	}

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
