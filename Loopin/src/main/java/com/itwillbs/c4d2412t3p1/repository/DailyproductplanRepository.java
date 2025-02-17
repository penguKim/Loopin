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
	@Query(value = "SELECT * FROM DAILYPRODUCTPLAN WHERE DAILYPRODUCTPLAN_SD = :productionDate", nativeQuery = true)
    List<Dailyproductplan> findByProductionDate(@Param("productionDate") Timestamp productionDate);
}
