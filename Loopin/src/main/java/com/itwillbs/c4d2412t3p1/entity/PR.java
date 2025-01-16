package com.itwillbs.c4d2412t3p1.entity;
import java.math.BigDecimal;
import java.security.Timestamp;

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
@Table(name = "PR")
public class PR {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pr_seq")
	@SequenceGenerator(name = "pr_seq", sequenceName = "pr_seq", allocationSize = 1)
	@Column(name = "pr_id")
	private long pr_id;
	
	@Column(name="pr_gm")
	private String pr_gm;
	
	@Column(name="pr_wm")
	private String pr_wm;
	
	@Column(name="pr_td")
	private BigDecimal pr_td;
	
	@Column(name="pr_ta")
	private BigDecimal pr_ta;
	
	@Column(name="pr_ns")
	private BigDecimal pr_ns;
	
	@Column(name="pr_tp")
	private BigDecimal pr_tp;

	@Column(name="pr_wr")
	private String pr_wr;
	
	@Column(name="pr_wd")
	private Timestamp pr_wd;
}
