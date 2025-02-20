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
public class InoutPK implements Serializable {
	
	private String inout_dt;
	private String inout_iw;
	private String inout_ow;
	private String item_cd;
	
}
