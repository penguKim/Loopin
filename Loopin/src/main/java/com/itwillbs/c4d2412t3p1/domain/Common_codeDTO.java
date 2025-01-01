package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Common_codeDTO {
	
	@JsonProperty("COMMON_GC")
	private String COMMON_GC;
	@JsonProperty("COMMON_CC")
	private String COMMON_CC;
	@JsonProperty("COMMON_NM")
	private String COMMON_NM;
	@JsonProperty("COMMON_CT")
	private String COMMON_CT;
	@JsonProperty("COMMON_IN")
	private String COMMON_IN;
	@JsonProperty("COMMON_US")
	private String COMMON_US;
	@JsonProperty("COMMON_RU")
	private String COMMON_RU;
	@JsonProperty("COMMON_RD")
	private Timestamp COMMON_RD;
	@JsonProperty("COMMON_UU")
	private String COMMON_UU;
	@JsonProperty("COMMON_UD")
	private Timestamp COMMON_UD;
	
	
	
}
