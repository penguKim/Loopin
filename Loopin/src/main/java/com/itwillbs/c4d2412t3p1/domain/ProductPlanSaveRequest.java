package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class ProductPlanSaveRequest {
	// 생산계획 정보
	private ProductPlanDTO productplan;

	// 공정 순서 목록
	private List<ProcessOrderDTO> processList;

	@Getter
	@Setter
	@ToString
	public static class ProductPlanDTO {
		private String contract_cd; // 수주번호
		private String product_cd; // 품목코드
		private String productplan_sd; // 시작일(문자열; parse 필요)
		private String productplan_ed; // 종료일
		private String productplan_dd; // 담당자
		private Integer productplan_js; // 지시수량
		private String warehouse_cd; // 창고코드
		private String productplan_st; // 상태
		private String productplan_bg; // 비고
		// 필요 시 productplan_wr, wd, mf, md 등도 추가
	}

	@Getter
	@Setter
	@ToString
	public static class ProcessOrderDTO {
		private String process_se; // 순서(문자열)
		private String process_cd; // 실제 공정코드
	}
}
