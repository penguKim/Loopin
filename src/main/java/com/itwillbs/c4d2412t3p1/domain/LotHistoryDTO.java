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
public class LotHistoryDTO {
	
	private String contract_cd;
	private String lot_cd;
	private String process_cd;
	private Timestamp lothistory_st;
	private Timestamp lothistory_en;
	private long lothistory_wq;
	private long lothistory_bq;
	
	public LotHistoryDTO() {}
}
