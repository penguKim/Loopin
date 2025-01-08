package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
import java.util.List;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ToString
public class PRDTO {

	private Long employee_id;
	private String employee_nm;
	private BigDecimal employee_bs;
	private List<PRCalDTO> calculated;
	
}
