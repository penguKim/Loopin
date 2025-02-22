package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.mapper.DashboardMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class DashboardService {
	
	private final DashboardMapper dashboardMapper;

	public Map<String, Object> select_total_EMPLOYEE(String currentCd) {
		
		Map<String, Object> employee = dashboardMapper.select_total_EMPLOYEE(currentCd);
		return employee;
	}
	public List<Map<String, Object>> select_EMPLOYEE_APPROVAL(Map<String, Object> params) {
		return dashboardMapper.select_EMPLOYEE_APPROVAL(params);
	}
	
}
