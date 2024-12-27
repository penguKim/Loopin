package com.itwillbs.c4d2412t3p1.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {
	
    List<Map<String, Object>> selectMembers();
    
}
