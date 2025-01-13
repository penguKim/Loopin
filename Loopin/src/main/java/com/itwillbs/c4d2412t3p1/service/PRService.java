package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.mapper.PRMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PRService {

	private final PRMapper prM;

	public List<Employee> select_empworklastmth() {

		List<Employee> list = prM.select_empworklastmth();
		
		return list;
	}

	public List<Map<String, Object>> select_worktimelastmth(List<String> employee_cdList) {

		List<Map<String, Object>> list = prM.select_wokringtimeformth(employee_cdList);
		
		return list;
	}
	
	
	
}
