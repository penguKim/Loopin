package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.mapper.CommonMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class CommonService {
	
	private final CommonMapper commonMapper;
	
	
	
	public List<Map<String, Object>> SELECT_COMMON_CODE(String code) {
		List<Map<String, Object>> list = commonMapper.SELECT_COMMON_CODE(code);
		
		return list;
	}
	

}
