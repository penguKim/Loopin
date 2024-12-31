package com.itwillbs.c4d2412t3p1.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonMapper {
	
    List<Map<String, Object>> SELECT_COMMON_CODE(@Param("code") String code);
    
}
