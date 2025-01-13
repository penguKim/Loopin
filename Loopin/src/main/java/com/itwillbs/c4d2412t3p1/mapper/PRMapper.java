package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.c4d2412t3p1.entity.Employee;

@Mapper
public interface PRMapper {

	List<Employee>select_empworklastmth();
	
	List<Map<String, Object>>select_wokringtimeformth(List<String> employee_cdList);
	
}
