package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.InoutDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;
import com.itwillbs.c4d2412t3p1.domain.WareareaDTO;
import com.itwillbs.c4d2412t3p1.domain.WarehouseDTO;

@Mapper
public interface InoutMapper {

	// 입출고 조회
	List<InoutDTO> select_INOUT_list();
	// 발주 조회
	List<InoutDTO> select_INOUT_ORDER();
	// 발주품목 조회
	List<InoutDTO> select_INOUT_ORDERDETAIL(@Param("order") String orderCd);
	// 수주 조회
	List<InoutDTO> select_INOUT_CONTRACT();
	// 수주품목 조회
//	List<InoutDTO> select_INOUT_CONTRACTDETAIL(@Param("contract") String contractCd);
	List<InoutDTO> select_INOUT_CONTRACTDETAIL(@Param("contractCd") String contractCd, @Param("productCd") String productCd, @Param("processCd") String processCd, 
			@Param("itemfullCd") String itemfullCd, @Param("itemCd") String itemCd, @Param("itemSz") String itemSz, @Param("itemCr") String itemCr);
	// 오더디테일 업데이트
	int updateOrderDetail(@Param("item") String item_cd, @Param("order") String inout_io, @Param("date") String date);
	// 오더디테일 미입고 카운트
	int select_ORDERDETAIL_count(@Param("order") String orderCd);
	// 출발창고 조회
	List<WareareaDTO> select_INOUT_OW(@Param("itemCd") String itemCd);
//	List<WareareaDTO> select_INOUT_IW(@Param("itemCd") String itemCd,@Param("list") List<WareareaDTO> list);
	// 도착창고 조회
	List<WareareaDTO> select_INOUT_IW(@Param("itemCd") String itemCd);
	// 수주건 로트조회
	List<InoutDTO> select_INOUT_LOT(@Param("contract") String contract_cd);


	
}