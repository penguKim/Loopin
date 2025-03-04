package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.c4d2412t3p1.entity.Workorder;

public interface WorkorderRepository extends JpaRepository<Workorder, String> {

	@Query(value = "SELECT WORKORDER_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();

	@Query("SELECT p.product_gc FROM Product p WHERE p.product_cd = :productCd AND p.product_sz = :productSz AND p.product_cr = :productCr")
	String findProductGcByProductCdAndProductSzAndProductCr(@Param("productCd") String productCd,
			@Param("productSz") String productSz, @Param("productCr") String productCr);
}
