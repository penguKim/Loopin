package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Warehouse;


@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {
	
	// 창고유형(warehouse_tp)이 'PROCESS'인 전체 창고 조회
    @Query(value = "SELECT * FROM warehouse WHERE warehouse_tp = 'PROCESS'", nativeQuery = true)
    List<Warehouse> select_WAREHOUSE_BY_TP();

    // 창고코드 조건과 함께, 창고유형이 'PROCESS'인 창고 조회
    @Query(value = "SELECT * FROM warehouse WHERE warehouse_cd = :warehouse_cd AND warehouse_tp = 'PROCESS'", nativeQuery = true)
    List<Warehouse> select_WAREHOUSE_BY_CD_TP(@Param("warehouse_cd") String warehouse_cd);
}
