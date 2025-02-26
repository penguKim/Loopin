package com.itwillbs.c4d2412t3p1.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LotResponse {
	private String lot_cd;        // LOT PK
    private String process_cd;    // LOT PK
    private String contract_cd;    // LOT PK
    private String lot_cr;        // 생성일시 (lot_cr) -> "yyyy-MM-dd" 등 문자열
    private String product_cd; // product_cd에서 베이스코드만 추출 or 그대로
    private String product_cr;    // 색상
    private String product_sz;    // 사이즈
    private Integer dailyproductplan_js;       // 수량 (productplan_js)
}
