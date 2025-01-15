package com.itwillbs.c4d2412t3p1.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ToString
public class PR_calculationMDTO {

//	private BigDecimal workingtime;
	@JsonProperty("BS")
    private String BS;
    
    @JsonProperty("overworkingtime")
    private String overworkingtime;
    
    @JsonProperty("nightworkingtime")
    private String nightworkingtime;
    
    @JsonProperty("weekendworkingtime")
    private String weekendworkingtime;
    
    @JsonProperty("holydayworkingtime")
    private String holydayworkingtime;
    
    @JsonProperty("leastannual")
    private String leastannual;	

    @JsonProperty("bonus")
    private String bonus;	
}
