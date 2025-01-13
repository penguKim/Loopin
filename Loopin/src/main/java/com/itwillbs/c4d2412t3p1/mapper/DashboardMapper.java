package com.itwillbs.c4d2412t3p1.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DashboardMapper {
	
    Map<String, Object> select_total_EMPLOYEE(String currentCd);
    
}
