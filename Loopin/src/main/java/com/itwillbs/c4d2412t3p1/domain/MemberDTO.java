package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberDTO {
	
	private String id;
	private String pass;
	private String name;
	private Timestamp dates;
	private String role;
}
