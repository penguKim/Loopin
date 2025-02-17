package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.EquipmentDTO;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.EquipmentFilterRequest;

@Mapper
public interface LotMapper {

	List<Map<String, Object>> select_LOT_list(Map<String, Object> params);

	List<Map<String, Object>> select_PROCESS_list();
	
	
}