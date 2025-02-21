package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
	
	private String order_cd;
	private String material_cd;
	private long material_am;
	private BigDecimal order_ct;
	private String order_ed;
	private String material_un;
	
}
