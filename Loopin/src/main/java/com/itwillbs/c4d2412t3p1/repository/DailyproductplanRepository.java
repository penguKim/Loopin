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

	@Query(value = """
			    SELECT *
			    FROM DAILYPRODUCTPLAN d
			    WHERE d.CONTRACT_CD = :contractCd
			      AND SUBSTR(d.PRODUCT_CD, 1, INSTR(d.PRODUCT_CD, '-', 1, 1) - 1) = :baseProductCd
			    ORDER BY d.DAILYPRODUCTPLAN_SD, d.PROCESS_CD, d.PRODUCT_CR, d.PRODUCT_SZ
			""", nativeQuery = true)
	List<Dailyproductplan> findAllByContractAndBaseProduct(@Param("contractCd") String contractCd,
			@Param("baseProductCd") String baseProductCd);
}
