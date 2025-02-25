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
public class MaterialDTO {
	
	private String material_cd;
	private String material_gc;
	private String material_cc;
	private String material_nm;
	private String material_sz;
	private String material_cr;
	private String material_gd;
	private String material_un;
	private String material_wh;
	private int material_pr;
	private String material_pc;
	private String material_rm;
	private boolean material_us;
	private String material_ru;
	private Timestamp material_rd;
	private String material_uu;
	private Timestamp material_ud;
	
	private String material_cn;
	private String warehouse_nm;
	
	public MaterialDTO() {}
	
	
}
