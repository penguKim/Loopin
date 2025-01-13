package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PRMapper {

	List<Map<String,Object>> selectpr(Long employee_cd);

	List<Map<String, Object>> checkprmodal(@Param("pr_id") Long pr_id, @Param("employee_cd") Long employee_cd);
}
