package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter 
@Setter
@ToString
public class InoutDTO {

	private String inout_dt; // 입출고일자
	private String inout_wh; // 출발창고
	private String item_cd; // 품목코드
	private String inout_wa; // 출발구역
	private String inout_co; // 수발주코드
	private String inout_tp; // 수발주구분
	private String lot_cd; // 로트번호
	private String process_cd; // 공정코드
	private Integer inout_nn; // 수량
	private Integer inout_in; // 이전총수량
	private Integer inout_fn; // 불량(사용할지고민)
	private String inout_io; // 출입고구분
	private String employee_cd; // 담당자
	private String inout_ru;
	private Timestamp inout_rd;
	private String inout_uu;
	private Timestamp inout_ud;	
	
	
	private String inout_whnm; // 출발창고명
	private String inout_wanm; // 출발구역명
	private String inout_tpnm; // 수발주구분명
	private String inout_ionm; // 출입고구분명
	private String item_un; // 기준단위
	private String item_nm; // 품목명
	private String item_gc; // 품목대분류
	private String item_cc; // 품목소분류
	private String item_gn; // 품목대분류명
	private String item_cn; // 품목소분류명
	private String employee_nm; // 담당자명
	private String account_cd; // 거래처코드
	private String account_nm; // 거래처명
	private String item_sz; // 사이즈
	private String item_cr; // 색상
	private String process_nm; // 공정이름
	private String product_cd; // 공정생산품목코드
	private String product_nm; // 공정생산품목명
	
	
	
}
