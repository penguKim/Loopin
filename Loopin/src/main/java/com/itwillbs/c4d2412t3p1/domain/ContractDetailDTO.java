package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;

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
public class ContractDetailDTO {
	
	private String contract_cd;
	private String product_cd;
	private String product_sz;
	private String product_cr;
	private long contract_am;
	private BigDecimal contract_ct;
	private String contract_ed;
	private String product_un;
	
	
}
