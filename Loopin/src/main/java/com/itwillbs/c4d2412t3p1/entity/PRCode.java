package com.itwillbs.c4d2412t3p1.entity;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@ToString
@Table(name="PRCODE")
public class PRCode {

	@Id
	@Column(name="prcode_id")
	private String prcode_id;
	
	@Column(name="prcode_nm")
	private String prcode_nm;

	@Column(name="prcode_nt")
	private boolean prcode_nt;
	
	@Column(name="prcode_gy")
	private String prcode_gy;
	
	@Column(name="prcode_fl")
	private String prcode_fl;
}
