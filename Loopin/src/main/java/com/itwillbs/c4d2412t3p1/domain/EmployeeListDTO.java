package com.itwillbs.c4d2412t3p1.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

// 생산계획 등록 시 담당자리스트를 가져오기 위한 DTO
public class EmployeeListDTO {
	private String employee_cd;
    private String employee_nm;
    private String employee_dp;
    private String employee_gd;
    private String departmentName;
    private String positionName;
}
