package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;

import com.itwillbs.c4d2412t3p1.util.FilterRequest.InoutFilterRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InoutRequestDTO {
	
	InoutFilterRequest inoutFilter;
	InoutDTO inout;
	List<WareareaDTO> wareareaList;
	List<InoutWarehouseDTO> iwList; // 입출고창고 리스트
	
	String order_cd;
	String contract_cd;
	String item_cd;
	String lot_cd;
	String product_cd;
	String item_cr;
	String item_sz;
	String process_cd;
	
}