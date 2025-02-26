package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EmployeeDTO {
	
	private String employee_cd;
	private String employee_id;
	private String employee_pw;
	private String employee_dp;
	private String employee_gd;
	private String employee_hd;
	private String employee_rd;
	private String employee_rr;
	private String employee_cg;
	private String employee_nt;
	private String employee_nm;
	private String employee_bd;
	private String employee_ad;
	private String employee_sb;
	private String employee_ph;
	private String employee_em;
	private String employee_pi;
	private BigDecimal employee_bs;
	private String employee_bk;
	private String employee_an;
	private String employee_dt;
	private String employee_wr;
	private Timestamp employee_wd;
	private String employee_mf;
	private Timestamp employee_md;
	private Boolean employee_mg; // 부서장 유무
	private String employee_rl; // 룰
	private Boolean employee_us; // 사용여부
	private String workinghour_id; // 근로관리
	
	
	private Long sequenceValue;
	
	
	// 사진 삭제 여부
	private String photoDeleted; // true or false (클라이언트에서 전달)
}
