package com.itwillbs.c4d2412t3p1.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InoutWarehouseDTO {

	private String ow_warehouse_cd; // 출발창고코드
	private String ow_warearea_cd; // 출발구역코드
	private String ow_warehouse_nm; // 출발창고명
	private String ow_warearea_nm; // 출발구역명
	private String ow_warearea_cp; // 최대적재량
	private String ow_stock_aq; // 품목적재량
	private int ow_inout_nn; // 출고수량
	private int ow_inout_fn; // 출고불량수
	private String iw_warehouse_cd; // 도착창고코드
	private String iw_warearea_cd; // 도착구역코드
	private String iw_warehouse_nm; // 도착창고명
	private String iw_warearea_nm; // 도착구역명
	private String iw_warearea_cp; // 최대적재량
	private String ow_warearea_lp; // 창고여유량
	private int iw_inout_nn; // 입고수량
	private int iw_inout_fn; // 입고불량수

	
	
}
