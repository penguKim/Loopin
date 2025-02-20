package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.c4d2412t3p1.domain.InoutDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;

@Mapper
public interface InoutMapper {

	// 입출고 조회
	List<InoutDTO> select_INOUT_list();

	// 발주 조회
	List<InoutDTO> select_INOUT_ORDER();

	// 발주품목 조회
	List<InoutDTO> select_INOUT_ORDERDETAIL();


	
}