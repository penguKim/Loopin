package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;
import com.itwillbs.c4d2412t3p1.entity.Attendance;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.mapper.AttendanceMapper;
import com.itwillbs.c4d2412t3p1.repository.AttendanceRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;

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

	public void insert_ANNUAL() {
		attendanceMapper.insert_ANNUAL();
	}

	public List<Map<String, Object>> select_EMPLOYEE(String employee_nm) {
		return attendanceMapper.select_EMPLOYEE(employee_nm);
	}

	public List<Map<String, Object>> select_ANNUAL(String employee_cd, String employee_dp, String employee_hd, String annual_ra) {
		return attendanceMapper.select_ANNUAL(employee_cd, employee_dp, employee_hd, annual_ra);
	}

//
//	public void delete_TRANSFER(List<Long> ids) {
//		
//		attendanceRepository.deleteAllById(ids);
//
//	}
}
