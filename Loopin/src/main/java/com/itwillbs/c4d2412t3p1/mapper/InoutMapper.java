package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.c4d2412t3p1.domain.InoutDTO;

@Mapper
public interface InoutMapper {

	// 입출고 조회
	List<InoutDTO> select_INOUT_list();


	
}