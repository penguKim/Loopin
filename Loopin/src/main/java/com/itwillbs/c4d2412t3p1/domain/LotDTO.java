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
public class LotDTO {
	
	private String lot_cd;
	private String process_cd;
	private String product_cd;
	private String contract_cd;
	private String product_cr;
	private String product_sz;
	private Integer productplan_js;
	private Timestamp lot_cr;
	private Timestamp lothistory_st;
	private Timestamp lothistory_en;
	private String lothistory_wq;
	private String lothistory_bq;
	
	public LotDTO() {}
}
