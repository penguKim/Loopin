package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;
import com.itwillbs.c4d2412t3p1.entity.Attendance;

@Mapper
public interface AttendanceMapper {

	List<Attendance> select_ANNUAL(String annual_yr);
	
	List<Map<String, Object>> insert_ANNUAL();

	List<Attendance> select_EMPLOYEE(String employee_id);
    
   
}