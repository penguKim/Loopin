package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderDTO {

	private String order_cd;
	private String account_cd;
	private String order_ps;
	private String order_sd;
	private String order_ed;
	private long order_am;
	private BigDecimal order_mn;
	private String order_st;
	private String order_rm;
	private String order_wr;
	private Timestamp order_wd;
	private String order_mf;
	private Timestamp order_md;
	
	
	private Long sequenceValue;
}
