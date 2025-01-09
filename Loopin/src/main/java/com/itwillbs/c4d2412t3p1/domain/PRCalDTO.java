package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ToString
public class PRCalDTO {


	private String prdetail_nm;
	
	private BigDecimal amount;

	public PRCalDTO(String prcode_nm, BigDecimal calculatedAmount) {
		this.prdetail_nm = prcode_nm;
		this.amount = calculatedAmount;
	}
}
