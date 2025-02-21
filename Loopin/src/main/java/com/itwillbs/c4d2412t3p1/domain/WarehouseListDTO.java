package com.itwillbs.c4d2412t3p1.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
// 생산계획 등록 시 창고정보를 가져오기 위한 DTO
public class WarehouseListDTO {
	private String warehouse_cd;
    private String warehouse_nm;
    private String warehouseName;
    private String warehouse_rm;
}
