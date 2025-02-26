package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
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
public class CommuteDTO {
	
	private String employee_cd;
	private String workinghour_id;
	private String commute_wd;
	private String commute_ld;
	private String commute_wt;
	private String commute_lt;
	private String commute_ig;
	private String commute_eg;
	private String commute_yg;
	private String commute_jg;
	private String commute_hg;
	private boolean commute_pr;
	private String commute_ru;
	private Timestamp commute_rd;
	private String commute_uu;
	private Timestamp commute_ud;
	
	private BigDecimal total_emp; // 총 사원
	private BigDecimal total; // 출근 인원
	private BigDecimal normal; // 정상출근
	private BigDecimal late; // 지각
	private BigDecimal working; // 근무중
	private BigDecimal leave; // 퇴근
	private String employee_nm;
	private String employee_dp;
	private String employee_gd;
	private String commute_st;
	
	
	
	public CommuteDTO() {}

	public CommuteDTO(String commute_wd, BigDecimal total) {
		super();
		this.commute_wd = commute_wd;
		this.total = total;
	};
	
	
}
