package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ToString
public class BomallDTO {

	private String product_cd;
	private String process_cd;
	private String bomprocess_cd;
	private String bomprocess_ap;
	private String bomprocess_rt;
	private Long bomprocess_ra;
	private String bomprocess_er;
	private String bomprocess_wr;
	private Timestamp bomprocess_wd;
	private String bomprocess_mf;
	private Timestamp bomprocess_md;
	private String bomprocess_bg;
	private String bom_cd;
	private Long bom_am;
}
