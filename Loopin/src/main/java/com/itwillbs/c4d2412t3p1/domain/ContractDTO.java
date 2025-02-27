package com.itwillbs.c4d2412t3p1.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ContractDTO {

	private String contract_cd;
	private String account_cd;
	private String employee_cd;
	private String contract_ps;
	private String contract_sd;
	private String contract_ed;
	private Long contract_am;
	private Long contract_mn;
	private String contract_st;
	private String contract_rm;
	private String contract_wr;
	private Timestamp contract_wd;
	private String contract_mf;
	private Timestamp contract_md;
	
	
	private Long sequenceValue;
}
