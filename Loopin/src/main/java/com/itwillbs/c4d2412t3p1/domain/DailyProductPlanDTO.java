package com.itwillbs.c4d2412t3p1.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DailyProductPlanDTO {
	private String dailyproductplan_sd; // 날짜 (문자열 "YYYY-MM-DD")
	private String contract_cd;
	private String product_cd;
	private String process_cd;
	private String product_cr;
	private String product_sz;

	private Integer dailyproductplan_js; // 작업수량
	private String process_se; // 공정순서 (추가)
}
