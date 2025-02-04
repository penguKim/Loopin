package com.itwillbs.c4d2412t3p1.entity;
import java.math.BigDecimal;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@ToString
@Table(name="PRDETAIL")
public class PRDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prd_seq")
	@SequenceGenerator(name = "prd_seq", sequenceName = "prd_seq", allocationSize = 1)
	@Column(name="prdetail_id")
	private long prdetail_id;
	
	@Column(name="pr_id")
	private long pr_id;
	
	@Column(name="employee_cd")
	private String employee_cd;

	@Column(name="employee_nm")
	private String employee_nm;
	
	@Column(name="prdetail_bs")
	private BigDecimal prdetail_bs;
	
	@Column(name="prdetail_mt")
	private BigDecimal prdetail_mt;
	
	@Column(name="prdetail_ot")
	private BigDecimal prdetail_ot;
	
	@Column(name="prdetail_na")
	private BigDecimal prdetail_na;

	@Column(name="prdetail_wa")
	private BigDecimal prdetail_wa;
	
	@Column(name="prdetail_ha")
	private BigDecimal prdetail_ha;
	
	@Column(name="prdetail_rl")
	private BigDecimal prdetail_rl;
	
	@Column(name="prdetail_bn")
	private BigDecimal prdetail_bn;
	
	@Column(name="prdetail_ta")
	private BigDecimal prdetail_ta;

	@Column(name="prdetail_gm")
	private BigDecimal prdetail_gm;
	
	@Column(name="prdetail_gy")
	private BigDecimal prdetail_gy;
	
	@Column(name="prdetail_gg")
	private BigDecimal prdetail_gg;

	@Column(name="prdetail_lg")
	private BigDecimal prdetail_lg;
	
	@Column(name="prdetail_td")
	private BigDecimal prdetail_td;
	
	@Column(name="prdetail_rs")
	private BigDecimal prdetail_rs;
	
	@Column(name="prdetail_ch")
	private boolean prdetail_ch;
	
}
