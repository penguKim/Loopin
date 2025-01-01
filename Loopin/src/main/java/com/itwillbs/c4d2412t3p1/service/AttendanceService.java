package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.mapper.AttendanceMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class AttendanceService {
	
	private final AttendanceMapper attendanceMapper;
	
	
	
	public List<Map<String, Object>> select_base_ATTENDANCE() {
		List<Map<String, Object>> list = attendanceMapper.select_base_ATTENDANCE();
		
		return list;
	}
	

}