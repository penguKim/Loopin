package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.AttendanceDTO;
import com.itwillbs.c4d2412t3p1.entity.Attendance;
import com.itwillbs.c4d2412t3p1.entity.Employee;

@Mapper
public interface AttendanceMapper {

	List<Map<String, Object>> select_EMPLOYEE(String employee_nm);
	
	List<Map<String, Object>> insert_ANNUAL();

	List<Map<String, Object>> select_ANNUAL(@Param("employee_cd")String employee_cd, 
			@Param("employee_dp")String employee_dp, @Param("employee_hd")String employee_hd, @Param("annual_ra")String annual_ra);
    
   
}