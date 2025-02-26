package com.itwillbs.c4d2412t3p1.entity;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class StockPK implements Serializable {
	
	private String item_cd;
	private String warehouse_cd;
	private String warearea_cd;
	
}
