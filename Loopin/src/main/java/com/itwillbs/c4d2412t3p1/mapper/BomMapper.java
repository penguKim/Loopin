package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.itwillbs.c4d2412t3p1.entity.BomProcess;
import com.itwillbs.c4d2412t3p1.entity.Product;

@Mapper
public interface BomMapper {

	List<BomProcess> selectBomAll();

	List<Product> selectPD();

	String checkpcd(String pcd);

	List<Process> selectPCs();

	List<Product> selectbom(@Param("ckrowpds") List<String> ckrowpds);

	List<Map<String, Object>> selectpcsfrompd(@Param("bpap") String bpap, @Param("pdcd") String pdcd, @Param("bppc") String bppc);

	List<Map<String, Object>> selectbomsformpd(@Param("pdcd") String pdcd, @Param("bpcd") String bpcd);


}
