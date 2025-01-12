package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PRMapper {

	List<Map<String, Object>> selectpradmin();

	List<Map<String, Object>> selectpradminfirstmodal(Long pr_id);

	List<Map<String, Object>> selectpradminfirstmodal2(Long prdetail_id);

}
