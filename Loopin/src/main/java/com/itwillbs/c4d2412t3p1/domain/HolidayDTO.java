package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class HolidayDTO {
	
	private String holiday_dt;
	private String holiday_nm;
	private String holiday_wa;
	private String holiday_aa;
	private String holiday_wr;
	private String holiday_wd;
	private String holiday_mf;
	private String holiday_md;
}
