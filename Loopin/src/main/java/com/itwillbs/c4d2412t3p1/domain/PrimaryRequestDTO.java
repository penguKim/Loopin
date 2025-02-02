package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;

import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.Warehouse;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.CommuteFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ProductFilterRequest;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.WarehouseFilterRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PrimaryRequestDTO {
	// 창고 관리
	WarehouseFilterRequest warehouseFilter;
	WarehouseDTO warehouse;
	List<WareareaDTO> wareareaList;
	List<WarehouseDTO> warehouseList;
	String warehouse_cd;

	// 제품관리
	ProductFilterRequest productFilter;
	ProductDTO product;
	List<String> sizeList;
	List<String> colorList;
}