package com.itwillbs.c4d2412t3p1.entity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter
@EqualsAndHashCode
public class BomId implements Serializable{

	private String product_cd;
	private String bomproduct_cd;
	private String bom_cd; 

}
