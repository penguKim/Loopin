package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WorktypeDTO {
	
	private String employee_cd;
	private String workinghour_id;
	private String worktype_ad;
	private String worktype_rm;
	private String worktype_ru;
	private Timestamp worktype_rd;
	private String worktype_uu;
	private Timestamp worktype_ud;
	
	private String employee_nm;
	private String employee_dp;
	private String employee_gd;
	private String workinghour_nm;
	private String workinghour_dw;
	
	
}
