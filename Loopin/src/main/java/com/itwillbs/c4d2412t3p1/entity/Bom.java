package com.itwillbs.c4d2412t3p1.entity;

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
@IdClass(BomId.class)
@Table(name = "BOM")
public class Bom {

	@Id
	@Column(name = "product_cd")
	private String product_cd;
	
	@Id
	@Column(name = "bom_cd")
	private String bom_cd;
	
	@Column(name = "bom_am")
	private String bom_am;
	
}
