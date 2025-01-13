package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApprovalDTO {
	
 	private String approval_cd;
	private String approval_sd;
	private String approval_ed;
	private String approval_dv;
	private String approval_tt;
	private String approval_ct;
	private String approval_wr;
	private Timestamp approval_wd;
	private String approval_mf;
	private Timestamp approval_md;  
	
	
	private Long sequenceValue;
	
}
