package com.itwillbs.c4d2412t3p1.domain;

import java.util.List;

import com.itwillbs.c4d2412t3p1.util.FilterRequest.EquipmentFilterRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EquipmentRequestDTO {
	EquipmentFilterRequest equipmentFilter;
	
	List<EquipmentDTO> equipmentList;
	EquipmentDTO equipment;
	String equipment_cd;
	
	List<WarehouseDTO> warehouseList;
}