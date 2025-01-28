package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Common_code_listDTO {
	
	private List<Common_codeDTO> createdRows;
	private List<Common_codeDTO> updatedRows;
	private List<Common_codeDTO> deletedRows;
	private String code;
	private String filter;
	
	
	
}
