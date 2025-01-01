package com.itwillbs.c4d2412t3p1.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;

import java.util.List;
import java.util.Map;

@Mapper
public interface CommonMapper {
	
	// 공통코드 조회
    List<Common_codeDTO> SELECT_COMMON_CODE(@Param("code") String code);
    // 공통코드 등록
    void insertCommonCodes(@Param("list") List<Common_codeDTO> createdRows);
    // 공통코드 삭제
	void delete_group_code(@Param("list")  List<Map<String, Object>> commonCodes);
    
}
