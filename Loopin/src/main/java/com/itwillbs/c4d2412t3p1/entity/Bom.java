package com.itwillbs.c4d2412t3p1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
	private long bom_am;
	
	@Id
	@Column(name = "bomproduct_cd")
	private String bomproduct_cd;
}
