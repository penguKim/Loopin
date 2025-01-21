package com.itwillbs.c4d2412t3p1.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ToString
public class PR_calculationMDTO {

	@JsonProperty("employee_cd")
	private String employee_cd;
	
	@JsonProperty("employee_nm")
	private String employee_nm;
	
	@JsonProperty("BS")
    private String BS;
    
	@JsonProperty("workingtime")
	private String workingtime;
	
    @JsonProperty("overworkingtime")
    private String overworkingtime;
    
    @JsonProperty("nightworkingtime")
    private String nightworkingtime;
    
    @JsonProperty("weekendworkingtime")
    private String weekendworkingtime;
    
    @JsonProperty("holydayworkingtime")
    private String holydayworkingtime;
    
    @JsonProperty("remainleave")
    private String remainleave;	

    @JsonProperty("bonus")
    private String bonus;	

    @JsonProperty("wm")
    private String wm;	
   
    @JsonProperty("wr")
    private String wr;	
}
