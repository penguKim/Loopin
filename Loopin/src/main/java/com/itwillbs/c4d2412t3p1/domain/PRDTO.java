package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
import java.util.List;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ToString
public class PRDTO {

	private String employee_cd;
	private String employee_nm;
	private BigDecimal employee_bs;
	private BigDecimal RS;
	private BigDecimal TA;
	private BigDecimal TD;
	private List<PRCalDTO> calculated;
	
}
