package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApprovalDTO {
	
	private String approval_cd;   // 결재코드
    private String approval_sd;   // 결재 시작일
    private String approval_ed;   // 결재 종료일
    private String approval_dv;   // 결재 구분
    private String approval_tt;   // 결재 제목
    private String approval_ct;   // 결재 내용
    private String approval_fa;   // 1차 결재권자 (employee_cd)
    private String approval_sa;   // 2차 결재권자 (employee_cd)
    private String approval_wr;   // 작성자
    private Timestamp approval_wd; // 작성일
    private String approval_mf;   // 수정자
    private Timestamp approval_md; // 수정일

    private Long sequenceValue;   // 시퀀스 값
	
}
