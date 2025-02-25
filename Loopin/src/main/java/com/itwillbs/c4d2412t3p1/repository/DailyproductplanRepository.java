package com.itwillbs.c4d2412t3p1.repository;

import java.sql.Timestamp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itwillbs.c4d2412t3p1.entity.Dailyproductplan;
import com.itwillbs.c4d2412t3p1.entity.DailyproductplanPK;

public interface DailyproductplanRepository extends JpaRepository<Dailyproductplan, DailyproductplanPK> {

	// 생산계획 일자로 조회
	@Query(value = "SELECT * FROM DAILYPRODUCTPLAN WHERE TO_CHAR(DAILYPRODUCTPLAN_SD, 'YYYY-MM-DD') = TO_CHAR(:productionDate, 'YYYY-MM-DD')", nativeQuery = true)
	List<Dailyproductplan> findByProductionDate(@Param("productionDate") Timestamp productionDate);

	@Query("""
			    SELECT d
			    FROM Dailyproductplan d
			    WHERE d.id.contract_cd = :contractCd
			      AND d.id.product_cd = :productCd
			    ORDER BY d.id.dailyproductplan_sd, d.id.process_cd, d.id.product_cr, d.id.product_sz
			""")
	List<Dailyproductplan> findAllByContractAndProduct(@Param("contractCd") String contractCd,
			@Param("productCd") String productCd);
}
