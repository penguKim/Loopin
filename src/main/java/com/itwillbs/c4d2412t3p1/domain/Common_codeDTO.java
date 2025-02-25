package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Common_codeDTO {
	
	private String common_gc;
	private String common_cc;
	private String common_nm;
	private String common_ct;
	private String common_in;
	private String common_us;
	private String common_ru;
	private Timestamp common_rd;
	private String common_uu;
	private Timestamp common_ud;
	
	private String beforeCommon_cc;
	
	
	public Common_codeDTO() {}


	public Common_codeDTO(String common_cc, String common_nm) {
		super();
		this.common_cc = common_cc;
		this.common_nm = common_nm;
	}


	
	
	
	
}
