package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.BomProcess;
import com.itwillbs.c4d2412t3p1.entity.BomProcessID;

@Repository
public interface BomProcessRepository extends JpaRepository<BomProcess, BomProcessID> {

	// product_cd가 일치하는 BOMPROCESS 목록 조회
	// 생산계획 등록 시 제품별 공정정보
    @Query(value = """
            WITH BOM_HIERARCHY (PRODUCT_CD, PROCESS_CD, BOMPROCESS_CD, BOMPROCESS_AP, BOMPROCESS_RT, LVL) AS (
              SELECT PRODUCT_CD,
                     PROCESS_CD,
                     BOMPROCESS_CD,
                     BOMPROCESS_AP,
                     BOMPROCESS_RT,
                     0 AS LVL
                FROM BOMPROCESS
               WHERE BOMPROCESS_AP = 'NONE'
                 AND PRODUCT_CD = :product_cd

              UNION ALL

              SELECT BP.PRODUCT_CD,
                     BP.PROCESS_CD,
                     BP.BOMPROCESS_CD,
                     BP.BOMPROCESS_AP,
                     BP.BOMPROCESS_RT,
                     BH.LVL + 1
                FROM BOMPROCESS BP
                JOIN BOM_HIERARCHY BH
                  ON BP.BOMPROCESS_AP = BH.PROCESS_CD
                 AND BP.PRODUCT_CD = BH.PRODUCT_CD
               WHERE BP.PRODUCT_CD = :product_cd
            )
            SELECT PRODUCT_CD,
                   PROCESS_CD,
                   BOMPROCESS_CD,
                   BOMPROCESS_AP,
                   BOMPROCESS_RT
              FROM BOM_HIERARCHY
             ORDER BY LVL
            """, nativeQuery = true)
        List<Object[]> selectRawData(@Param("product_cd") String productCd);
}
