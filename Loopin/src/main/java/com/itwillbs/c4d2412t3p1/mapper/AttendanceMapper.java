package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AttendanceMapper {
	
    List<Map<String, Object>> select_base_ATTENDANCE();
    
}