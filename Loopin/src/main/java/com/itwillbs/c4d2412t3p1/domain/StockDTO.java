package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
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
	private BigDecimal stock_aq;
	private BigDecimal stock_mq;
	private String stock_ru;
	private Timestamp stock_rd;
	private String stock_uu;
	private Timestamp stock_ud;
	
}
