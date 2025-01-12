package com.itwillbs.c4d2412t3p1.entity;
import java.math.BigDecimal;
import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@ToString
@Table(name="PRDETAIL")
public class PRDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="prdetail_id")
	private long prdetail_id;
	
	@Column(name="pr_id")
	private long pr_id;
	
	@Column(name="employee_cd")
	private long employee_cd;
	
	@Column(name="prdetail_bs")
	private BigDecimal predetail_bs;
	//.setScale(0,BigDecimal.ROUND_HALF_UP); => 반올림 함수
	
	@Column(name="prdetail_mt")
	private BigDecimal predetail_mt;
	
	@Column(name="prdetail_ot")
	private BigDecimal predetail_ot;
	
	@Column(name="prdetail_na")
	private BigDecimal predetail_na;
	@Column(name="prdetail_wa")
	private BigDecimal predetail_wa;
	
	@Column(name="prdetail_ha")
	private BigDecimal predetail_ha;
	
	@Column(name="prdetail_aa")
	private BigDecimal predetail_aa;
	
	@Column(name="prdetail_bn")
	private BigDecimal predetail_bn;
	
	@Column(name="prdetail_ta")
	private BigDecimal predetail_ta;
	@Column(name="prdetail_gm")
	private BigDecimal predetail_gm;
	
	@Column(name="prdetail_gy")
	private BigDecimal predetail_gy;
	
	@Column(name="prdetail_gg")
	private BigDecimal predetail_gg;
	@Column(name="prdetail_lg")
	private BigDecimal predetail_lg;
	
	@Column(name="prdetail_td")
	private BigDecimal predetail_td;
	
	@Column(name="prdetail_rs")
	private BigDecimal predetail_rs;
	
	@Column(name="prdetail_ch")
	private boolean predetail_ch;
	
}