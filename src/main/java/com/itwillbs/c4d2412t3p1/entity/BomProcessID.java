package com.itwillbs.c4d2412t3p1.entity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("serial")
@Getter @Setter
@EqualsAndHashCode
public class BomProcessID implements Serializable{

	private String product_cd;
	private String process_cd;
	private String bomprocess_cd;
	private String bomprocess_ap;
}
