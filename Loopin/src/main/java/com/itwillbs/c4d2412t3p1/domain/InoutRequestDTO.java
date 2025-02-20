package com.itwillbs.c4d2412t3p1.domain;

import com.itwillbs.c4d2412t3p1.util.FilterRequest.InoutFilterRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InoutRequestDTO {
	
	InoutFilterRequest inoutfilter;
	InoutDTO inout;
	
}