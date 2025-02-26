package com.itwillbs.c4d2412t3p1.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BomMaterialDTO {
	// 생산 계획 등록 모달창에서 특정 완제품에 대한 전체 원자재, 부자재 소요량 출력에 사용되는 DTO
	private String material_cd;   // 자재 코드
    private String material_nm;   // 자재 이름
    private String material_gc;   // 자재 유형 ('MATERIALS', 'SUBMAT')
    private Long totalAmount;     // 최종 소요량(누적)
}
