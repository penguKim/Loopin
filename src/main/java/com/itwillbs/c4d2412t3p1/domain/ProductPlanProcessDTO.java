package com.itwillbs.c4d2412t3p1.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductPlanProcessDTO {
	private String process_cd; // 공정 코드
    private String process_se; // 공정 순서
}
