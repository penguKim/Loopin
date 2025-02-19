package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ToString
public class InoutDTO {

	private String inout_dt; // 입출고일자
	private String inout_iw; // 출발창고
	private String inout_ow; // 도착창고
	private String item_cd; // 품목코드
	private String inout_ia; // 출발구역
	private String inout_oa; // 도착구역
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
	
	
	private String inout_iwnm; // 출발창고명
	private String inout_ianm; // 출발구역명
	private String inout_ownm; // 도착창고명
	private String inout_oanm; // 도착구역명
	private String inout_tpnm; // 수발주구분명
	private String inout_ionm; // 출입고구분명
	private String item_un; // 기준단위
	private String item_nm; // 품목명
	private String item_gn; // 품목대분류명
	private String item_cn; // 품목소분류명
	private String employee_nm; // 담당자명
	
	
}
