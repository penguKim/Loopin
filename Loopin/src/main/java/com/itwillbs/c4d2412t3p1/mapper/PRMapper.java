package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.c4d2412t3p1.entity.Employee;

import org.apache.ibatis.annotations.Param;

@Mapper
public interface PRMapper {

	List<Employee>select_empworklastmth();
	
	List<Map<String, Object>>select_wokringtimeformth(@Param("employee_cdList") List<String> employee_cdList);
	
	List<Map<String, Object>> selectpradmin();

	List<Map<String, Object>> selectpradminfirstmodal(Long pr_id);

	List<Map<String, Object>> selectpradminfirstmodal2(@Param("prdetail_id") Long prdetail_id);

	
	List<Map<String,Object>> selectpr(String employee_cd);

	List<Map<String, Object>> checkprmodal(@Param("pr_id") Long pr_id, @Param("employee_cd") String employee_cd);

}
