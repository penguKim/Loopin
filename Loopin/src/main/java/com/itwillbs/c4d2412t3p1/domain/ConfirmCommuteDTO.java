package com.itwillbs.c4d2412t3p1.domain;

import groovy.transform.ToString;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@ToString
public class ConfirmCommuteDTO {

	private String employee_cd;
	 private String workingtime;
	 private String overworkingtime;
	 private String nightworkingtime; 
	 private String weekendworkingtime;
     private String holydayworkingtime;
     private String leastannual;
}
