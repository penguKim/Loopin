package com.itwillbs.c4d2412t3p1.entity;

import java.security.Timestamp;

import groovy.transform.ToString;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@ToString
@IdClass(BomProcessID.class)
@Table(name = "BOMPROCESS")
public class BomProcess {

	@Id
	@Column(name = "product_cd")
	private String product_cd;
	
	@Id
	@Column(name = "process_cd")
	private String process_cd;
	
	@Id
	@Column(name = "bomprocess_cd")
	private String bomprocess_cd;
	
	@Id
	@Column(name = "bomprocess_ap")
	private String bomprocess_ap;
	
	@Column(name = "bomprocess_rt")
	private String bomprocess_rt;
	
	@Column(name = "bomprocess_ra")
	private Long bomprocess_ra;
	
	@Column(name = "bomprocess_er")
	private String bomprocess_er;
	
	@Column(name = "bomprocess_wr")
	private String bomprocess_wr;
	
	@Column(name = "bomprocess_wd")
	private Timestamp bomprocess_wd;
	
	@Column(name = "bomprocess_mf")
	private String bomprocess_mf;
	
	@Column(name = "bomprocess_md")
	private Timestamp bomprocess_md;
	
}
