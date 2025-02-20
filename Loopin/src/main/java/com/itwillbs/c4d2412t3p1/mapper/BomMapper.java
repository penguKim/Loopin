package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;

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


}
