package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;
import com.itwillbs.c4d2412t3p1.entity.Attendance;
import com.itwillbs.c4d2412t3p1.mapper.AttendanceMapper;
import com.itwillbs.c4d2412t3p1.repository.AttendanceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final AttendanceMapper attendanceMapper;

	public List<Attendance> findAll() {

		return attendanceRepository.findAll();
	}
	
	public List<Attendance> select_ANNUAL(String annual_yr) {
		
		return attendanceMapper.select_ANNUAL(annual_yr);
	}

	public void insert_ANNUAL() {

		attendanceMapper.insert_ANNUAL();

	}

	public List<Attendance> select_EMPLOYEE(String employee_id) {
		return attendanceMapper.select_EMPLOYEE(employee_id);
	}
//
//	public void delete_TRANSFER(List<Long> ids) {
//		
//		attendanceRepository.deleteAllById(ids);
//
//	}
}
