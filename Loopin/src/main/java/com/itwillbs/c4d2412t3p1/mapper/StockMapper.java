package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.MaterialDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;
import com.itwillbs.c4d2412t3p1.entity.Stock;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.StockFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

@Mapper
public interface StockMapper {

	// 재고 조회
	List<Map<String, Object>> select_STOCK_list(@Param("filter") StockFilterRequest filter, @Param("sizeList") Map<String, List<Common_codeDTO>> sizeList);

	List<Map<String, Object>> select_STOCK_chart(@Param("filter") StockFilterRequest filter);

	// 품목 조회
	List<StockDTO> select_STOCK_MATERIAL(@Param("material_gc") String material_gc, @Param("material_cc") String material_cc, @Param("filter") StockFilterRequest filter);
	List<StockDTO> select_STOCK_PRODUCT(@Param("product_gc") String product_gc, @Param("product_cc") String product_cc, @Param("filter") StockFilterRequest filter);

	// 창고 재고 체크
	StockDTO check_STOCK_AQ(@Param("stock") StockDTO stock);




	
}