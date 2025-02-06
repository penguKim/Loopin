package com.itwillbs.c4d2412t3p1.entity;

import java.security.Timestamp;

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
@Table(name = "PROCESS")
public class Process {
	
	@Id
	@Column(name = "process_cd")
	private String process_cd;
	
	@Column(name = "process_nm")
	private String process_nm;
	
	@Column(name = "material_gc")
	private String material_gc;
	
	@Column(name = "material_cc")
	private String material_cc;
	
	@Column(name = "process_us")
	private boolean process_us;
	
	@Column(name = "process_eq")
	private String process_eq;
	
	@Column(name = "process_pd")
	private String process_pd;
	
	@Column(name = "process_bg")
	private String process_bg;
	
	@Column(name = "process_wr")
	private String process_wr;
	
	@Column(name = "process_wd")
	private Timestamp process_wd;
	
	@Column(name = "process_mf")
	private String process_mf;
	
	@Column(name = "process_md")
	private Timestamp process_md;
	
}
