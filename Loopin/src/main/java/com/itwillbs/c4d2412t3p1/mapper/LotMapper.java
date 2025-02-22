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

	Map<String, Object> select_LOT_json(Map<String, Object> params);

	List<Map<String, Object>> select_RESULT_list(Map<String, Object> params);

	List<Map<String, Object>> select_ACCOUNT_list();

	List<Map<String, Object>> select_LOTHISTORY_list(String lot_cd);

	List<Map<String, Object>> select_RESULT_detail(String contract_cd);
	
	
}