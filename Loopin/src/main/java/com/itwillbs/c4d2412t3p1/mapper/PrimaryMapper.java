package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.MaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

@Mapper
public interface PrimaryMapper {

	// 창고 관리 ----------------------------
	// 창고 조회
	List<WarehouseDTO> select_WAREHOUSE_list(@Param("filter") WarehouseFilterRequest filter);


	// 창고목록 조회
	List<Map<String, String>> select_WAREHOUSE_code();

	// 품목 관리 ----------------------------
	// 제품 조회
	List<ProductDTO> select_PRODUCT_list(@Param("filter") ProductFilterRequest filter);
	
	// 자재 조회 
	List<MaterialDTO> select_MATERIAL_list(@Param("filter") ProductFilterRequest filter);
	
	// 품목 사진 조회
	ProductDTO select_PRODUCT_PC(@Param("product_cd") String product_cd);

	
}