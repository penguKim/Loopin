package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.domain.ProductPlanProcessDTO;
import com.itwillbs.c4d2412t3p1.entity.Productplanprocess;
import com.itwillbs.c4d2412t3p1.entity.ProductplanprocessPK;

import jakarta.transaction.Transactional;

@Repository
public interface ProductplanprocessRepository extends JpaRepository<Productplanprocess, ProductplanprocessPK> {
//	(contract_cd, product_cd)가 같은 레코드들 삭제
//   process_se 상관없이
	@Transactional
	@Modifying
	@Query("DELETE FROM Productplanprocess p " + "WHERE p.id.contract_cd = :contractCd "
			+ "AND p.id.product_cd = :productCd")
	void deleteByIdContractCdAndIdProductCd(@Param("contractCd") String contractCd,
			@Param("productCd") String productCd);

	// (contract_cd, product_cd) 공정순서 목록
	@Query("SELECT p FROM Productplanprocess p " + "WHERE p.id.contract_cd = :contractCd "
			+ "  AND p.id.product_cd  = :productCd")
	List<Productplanprocess> findByContractCdAndProductCd(@Param("contractCd") String contractCd,
			@Param("productCd") String productCd);

	@Query("SELECT new com.itwillbs.c4d2412t3p1.domain.ProductPlanProcessDTO(p.process_cd, p.id.process_se) "
			+ "FROM Productplanprocess p " + "WHERE p.id.contract_cd = :contractCd "
			+ "AND p.id.product_cd = :productCd " + "ORDER BY p.id.process_se")
	List<ProductPlanProcessDTO> findProcessesByContractAndProduct(@Param("contractCd") String contractCd,
			@Param("productCd") String productCd);

}
