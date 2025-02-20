package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;
import java.util.Map;

import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.StockFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StockRequestDTO {

	StockFilterRequest stockFilter;
	StockDTO stock;
	List<Map<String, Object>> stockList;
	
	
	String warehouse_cd;
	String material_gc;
	String material_cc;
	String product_gc;
	String product_cc;
}