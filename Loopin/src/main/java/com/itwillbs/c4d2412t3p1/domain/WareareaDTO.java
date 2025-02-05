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
	private String warearea_cp;
	private String warearea_in;
	private boolean warearea_us;
	private String warearea_rm;
	
	
	
	public WareareaDTO() {}
	
	
}
