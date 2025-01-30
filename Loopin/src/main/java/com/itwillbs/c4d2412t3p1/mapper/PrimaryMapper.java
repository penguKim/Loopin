package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

@Mapper
public interface PrimaryMapper {

	// 창고 조회
	List<WarehouseDTO> select_WAREHOUSE_list(@Param("filter") WarehouseFilterRequest filter);
	
}