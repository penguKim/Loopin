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
public class ProductDTO {
	
	private String product_cd;
	private String product_gc;
	private String product_cc;
	private String product_nm;
	private String product_sz;
	private String product_cr;
	private String product_gd;
	private String product_un;
	private String product_wh;
	private int product_pr;
	private String product_pc;
	private String product_rm;
	private boolean product_us;
	private String product_ru;
	private Timestamp product_rd;
	private String product_uu;
	private Timestamp product_ud;
	private String product_fd;
	
	private String product_crnm;
	private String product_gn;
	private String product_cn;
	private String warehouse_nm;
	
	
	public ProductDTO() {}
	
	
}
