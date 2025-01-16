package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NoticeDTO {
	
 	private String notice_cd;
	private String notice_tt;
	private String notice_ct;
//	private String notice_fl;
	private String notice_wr;
	private Timestamp notice_wd;
	private String notice_mf;
	private Timestamp notice_md;  
	
	
	private Long sequenceValue;
	
	
//	private String fileDeleted; // true or false (클라이언트에서 전달)
}
