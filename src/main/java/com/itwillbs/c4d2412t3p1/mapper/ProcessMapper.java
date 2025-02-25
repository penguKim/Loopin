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

	List<Map<String, Object>> selecteqlist(@Param("pd") String pd);

	int postProcess(@Param("regidata") List<Map<String, Object>> regidata);

	String checkpccd(@Param("cdvalue") String cdvalue);

	List<Map<String, Object>> selectpc(@Param("pccd") String pccd);

}
