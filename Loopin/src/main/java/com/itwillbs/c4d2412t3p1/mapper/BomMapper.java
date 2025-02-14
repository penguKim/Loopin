package com.itwillbs.c4d2412t3p1.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.itwillbs.c4d2412t3p1.entity.Product;

@Mapper
public interface BomMapper {

	List<Product> selectPD();

}
