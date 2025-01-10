package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommuteDTO {
	
	private String employee_cd;
	private String workinghour_id;
	private String commute_wd;
	private String commute_ld;
	private String commute_wt;
	private String commute_lt;
	private String commute_ru;
	private Timestamp commute_rd;
	private String commute_uu;
	private Timestamp commute_ud;
	
	private BigDecimal total;
	private BigDecimal inside;
	private BigDecimal outside;
	
	
	public CommuteDTO() {}

	public CommuteDTO(String commute_wd, BigDecimal total) {
		super();
		this.commute_wd = commute_wd;
		this.total = total;
	};

	
}
