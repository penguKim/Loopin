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
public class ContractDetailDTO {
	
	private String contract_cd;
	private String product_cd;
	private String product_sz;
	private String product_cr;
	private Long product_am;
	private Long contract_ct;
	private String contract_ed;
	private String product_un;
	
	
}
