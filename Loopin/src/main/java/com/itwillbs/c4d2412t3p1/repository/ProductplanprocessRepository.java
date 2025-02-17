package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Productplanprocess;
import com.itwillbs.c4d2412t3p1.entity.ProductplanprocessPK;

import jakarta.transaction.Transactional;

@Repository
public interface ProductplanprocessRepository extends JpaRepository<Productplanprocess, ProductplanprocessPK> {
//	(contract_cd, product_cd)가 같은 레코드들 삭제
//   process_se 상관없이
	@Transactional
    @Modifying
    @Query("DELETE FROM Productplanprocess p "
         + "WHERE p.id.contract_cd = :contractCd "
         + "AND p.id.product_cd = :productCd")
    void deleteByIdContractCdAndIdProductCd(@Param("contractCd") String contractCd,
                                            @Param("productCd") String productCd);
}
