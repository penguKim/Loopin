package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorkinghourDTO {
	
	private String workinghour_id;
	private String workinghour_nm;
	private String workinghour_wt;
	private String workinghour_lt;
	private String workinghour_wp;
	private String workinghour_hs;
	private String workinghour_dw;
	private String workinghour_tt;
	private String workinghour_hu;
	private String workinghour_us;
	private String workinghour_rm;
	private String workinghour_ru;
	private Timestamp workinghour_rd;
	private String workinghour_uu;
	private Timestamp workinghour_ud;
	
	private List<String> week;
	private String employee_cd;
	private String employee_nm;
	private String employee_dp;
	private String employee_gd;
	private String employee_cnt;
}
