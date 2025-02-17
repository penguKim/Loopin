package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ToString
public class StockDTO {

	private String item_cd;
	private String warehouse_cd;
	private String warearea_cd;
	private int stock_aq;
	private int stock_mq;
	private String stock_ru;
	private Timestamp stock_rd;
	private String stock_uu;
	private Timestamp stock_ud;
	private String stock_tp;
	
	
	private String item_nm;
	private String item_gn;
	private String item_cn;
	private String warehouse_nm;
	private String warearea_nm;
	private String color_nm;
	private String item_un;
	private String color;

	
}
