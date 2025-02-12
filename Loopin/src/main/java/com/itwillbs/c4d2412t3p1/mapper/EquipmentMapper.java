package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.EquipmentDTO;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.EquipmentFilterRequest;

@Mapper
public interface EquipmentMapper {
	
	List<EquipmentDTO> select_EQUIPMENT(@Param("filter") EquipmentFilterRequest filter);

}