package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Common_codeDTO {
	
//	@JsonProperty("COMMON_GC")
	private String common_gc;
//	@JsonProperty("COMMON_CC")
	private String common_cc;
//	@JsonProperty("COMMON_NM")
	private String common_nm;
//	@JsonProperty("COMMON_CT")
	private String common_ct;
//	@JsonProperty("COMMON_IN")
	private String common_in;
//	@JsonProperty("COMMON_US")
	private String common_us;
//	@JsonProperty("COMMON_RU")
	private String common_ru;
//	@JsonProperty("COMMON_RD")
	private Timestamp common_rd;
//	@JsonProperty("COMMON_UU")
	private String common_uu;
//	@JsonProperty("COMMON_UD")
	private Timestamp common_ud;
	
	private String beforeCommon_cc;
	
	
	public Common_codeDTO() {}


	public Common_codeDTO(String common_cc, String common_nm) {
		super();
		this.common_cc = common_cc;
		this.common_nm = common_nm;
	}


	
	
	
	
}
