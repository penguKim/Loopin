package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class WarehouseDTO {
	
	private String warehouse_cd;
	private String warehouse_nm;
	private String warehouse_tp;
	private String warehouse_in;
	private boolean warehouse_us;
	private String warehouse_rm;
	private String warehouse_ru;
	private Timestamp warehouse_rd;
	private String warehouse_uu;
	private Timestamp warehouse_ud;

	private String wacount;
	private String warehouse_tpnm;
	
	public WarehouseDTO() {}
	
	
}
