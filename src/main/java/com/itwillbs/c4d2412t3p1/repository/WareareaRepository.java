package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Warearea;
import com.itwillbs.c4d2412t3p1.entity.WareareaPK;


@Repository
public interface WareareaRepository extends JpaRepository<Warearea, WareareaPK> {

	@Modifying
    @Query("DELETE FROM Warearea w WHERE w.warehouse_cd = :warehouseCd")
    void deleteByWarehouseCd(@Param("warehouseCd") String warehouseCd);

    @Query("SELECT w FROM Warearea w WHERE w.warehouse_cd = :warehouseCd")
    List<Warearea> findByWarehouseCd(@Param("warehouseCd") String warehouseCd);
    
    @Modifying
    @Query("DELETE FROM Warearea w WHERE w.warehouse_cd IN :warehouseCodes")
    void deleteByWarehouseCdIn(@Param("warehouseCodes") List<String> warehouseCodes);

}
