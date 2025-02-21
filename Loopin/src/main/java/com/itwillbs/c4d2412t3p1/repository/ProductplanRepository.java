package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.domain.WorkableEmployeeProjection;
import com.itwillbs.c4d2412t3p1.entity.Productplan;
import com.itwillbs.c4d2412t3p1.entity.ProductplanPK;

@Repository
public interface ProductplanRepository extends JpaRepository<Productplan, ProductplanPK> {
	@Query(value = """
			WITH AVAILABLE_EMPLOYEES AS (
			    SELECT E.EMPLOYEE_ID, E.EMPLOYEE_CD, E.EMPLOYEE_NM
			    FROM EMPLOYEE E
			    WHERE E.WORKINGHOUR_ID = 'BLUE'
			      -- 휴가 승인된 사원 제외
			      AND NOT EXISTS (
			          SELECT 1
			          FROM APPROVAL A
			          WHERE A.APPROVAL_DV = '10'        -- 휴가신청서
			            AND A.APPROVAL_AV = '40'       -- 승인 상태
			            AND A.APPROVAL_WR = E.EMPLOYEE_ID
			            AND TO_DATE(:workDate, 'YYYY-MM-DD') BETWEEN
			                TO_DATE(JSON_VALUE(A.APPROVAL_CT, '$.startday'), 'YYYY-MM-DD')
			            AND TO_DATE(JSON_VALUE(A.APPROVAL_CT, '$.endday'), 'YYYY-MM-DD')
			      )
			),
			WORK_HOURS AS (
			    SELECT 'BLUE' AS WORKINGHOUR_ID,
			           (TO_NUMBER(TO_CHAR(TO_DATE(WORKINGHOUR_LT, 'HH24:MI:SS'), 'HH24'))
			            - TO_NUMBER(TO_CHAR(TO_DATE(WORKINGHOUR_WT, 'HH24:MI:SS'), 'HH24')) - 1) AS TOTAL_HOURS
			    FROM WORKINGHOUR
			    WHERE WORKINGHOUR_ID = 'BLUE'
			),
			CURRENT_WORKLOAD AS (
			    SELECT DP.DAILYPRODUCTPLAN_JB AS EMPLOYEE_ID,
			           SUM(BP.BOMPROCESS_RT) AS TOTAL_HOURS_USED
			    FROM DAILYPRODUCTPLAN DP
			    JOIN BOMPROCESS BP
			        ON DP.PRODUCT_CD = BP.PRODUCT_CD
			       AND DP.PROCESS_CD = BP.PROCESS_CD
			    WHERE TRUNC(DP.DAILYPRODUCTPLAN_SD) = TO_DATE(:workDate, 'YYYY-MM-DD')
			    GROUP BY DP.DAILYPRODUCTPLAN_JB
			),
			NEW_PROCESS_TIME AS (
			    SELECT BOMPROCESS_RT AS NEW_PROCESS_TIME
			    FROM BOMPROCESS
			    WHERE PRODUCT_CD = :productCd
			      AND PROCESS_CD = :processCd
			)
			SELECT AE.EMPLOYEE_ID       AS employee_id,
			       AE.EMPLOYEE_CD       AS employee_cd,
			       AE.EMPLOYEE_NM       AS employee_nm,
			       WH.TOTAL_HOURS       AS total_hours,
			       NVL(CW.TOTAL_HOURS_USED, 0)  AS total_hours_used,
			       NP.NEW_PROCESS_TIME  AS new_process_time,
			       (WH.TOTAL_HOURS - NVL(CW.TOTAL_HOURS_USED, 0)) AS remaining_hours,
			       (WH.TOTAL_HOURS - NVL(CW.TOTAL_HOURS_USED, 0) - NP.NEW_PROCESS_TIME) AS final_remaining_hours
			FROM AVAILABLE_EMPLOYEES AE
			JOIN WORK_HOURS WH ON WH.WORKINGHOUR_ID = 'BLUE'
			LEFT JOIN CURRENT_WORKLOAD CW ON AE.EMPLOYEE_ID = CW.EMPLOYEE_ID
			CROSS JOIN NEW_PROCESS_TIME NP
			WHERE (WH.TOTAL_HOURS - NVL(CW.TOTAL_HOURS_USED, 0) - NP.NEW_PROCESS_TIME) >= 0
			""", nativeQuery = true)
	List<WorkableEmployeeProjection> findWorkableEmployees(@Param("workDate") String workDate,
			@Param("productCd") String productCd, @Param("processCd") String processCd);

}
