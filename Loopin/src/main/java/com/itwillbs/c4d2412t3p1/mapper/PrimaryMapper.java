package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

@Mapper
public interface PrimaryMapper {

	// 창고 조회
	List<WarehouseDTO> select_WAREHOUSE_list(@Param("filter") WarehouseFilterRequest filter);

	// 제품 조회
	List<ProductDTO> select_PRODUCT_list(@Param("filter") ProductFilterRequest filter);

	// 창고목록 조회
	List<Map<String, String>> select_WAREHOUSE_code();
	
}