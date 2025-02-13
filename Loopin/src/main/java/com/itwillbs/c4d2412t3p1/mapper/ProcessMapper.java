package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProcessMapper {

	List<Map<String, Object>> selectpclist();

	List<Map<String, Object>> selectpdgclist(String COMMON_GC);

	List<Map<String, Object>> selectpdcclist(@Param("pdcc") String pdcc, @Param("pdgc") String pdgc);

	List<Map<String, Object>> selecteqlist(@Param("pd")List pd);

	List<Map<String, Object>> postprocess(Map<String, Object> regidata);

}
