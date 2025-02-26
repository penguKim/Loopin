package com.itwillbs.c4d2412t3p1.domain;

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
public class WareareaDTO {
	
	private String warehouse_cd;
	private String warearea_cd;
	private String warearea_nm;
	private Integer warearea_cp;
	private String warearea_in;
	private boolean warearea_us;
	private String warearea_rm;
	
	
	private String warehouse_nm;
	private int stock_aq;
	private int warearea_lp; // 창고여유수량
	private int inout_nn; // 입출고수량
	
	public WareareaDTO() {}
	
	
}
