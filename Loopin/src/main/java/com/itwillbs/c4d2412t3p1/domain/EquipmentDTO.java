package com.itwillbs.c4d2412t3p1.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class EquipmentDTO {
	
	private String equipment_cd;
	private String model_cd;
	private String equipment_nm;
	private String warehouse_cd;
	private boolean equipment_us;
	private String product_cd;
	private String equipment_rm;
	private String equipment_pc;
	private String equipment_ru;
	private Timestamp equipment_rd;
	private String equipment_uu;
	private Timestamp equipment_ud;
	
	public EquipmentDTO() {}
}
