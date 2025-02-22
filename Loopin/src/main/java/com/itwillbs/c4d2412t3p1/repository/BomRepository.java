package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.domain.BomallDTO;
import com.itwillbs.c4d2412t3p1.entity.Bom;
import com.itwillbs.c4d2412t3p1.entity.BomId;

@Repository
public interface BomRepository extends JpaRepository<Bom, BomId> {
//	 특정 제품(Product)의 BOM 조회
	@Query("SELECT b FROM Bom b WHERE b.product_cd = :productCd")
	List<Bom> findByProductCd(@Param("productCd") String productCd);

//	 특정 제품(Product) BOM 재귀 조회 (Oracle CONNECT BY 방식)
	@Query(value = """
			    SELECT LEVEL,
			           b.product_cd AS productCd,
			           b.bom_cd AS bomCd,
			           b.bom_am AS bomAm
			      FROM BOM b
			     START WITH b.product_cd = :productCd
			   CONNECT BY PRIOR b.bom_cd = b.product_cd
			""", nativeQuery = true)
	List<Object[]> findRecursiveBomByProductCd(@Param("productCd") String productCd);

	void save(BomallDTO bomItem);

}
