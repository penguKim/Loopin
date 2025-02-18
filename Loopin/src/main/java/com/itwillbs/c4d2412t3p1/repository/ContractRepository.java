package com.itwillbs.c4d2412t3p1.repository;


import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Contract;
import com.itwillbs.c4d2412t3p1.entity.ContractDetail;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.ContractFilterRequest;

import jakarta.transaction.Transactional;

@Repository
public interface ContractRepository  extends JpaRepository<Contract, String> {

	// 시퀀스 조회
	@Query(value = "SELECT CT_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();
	
    // 수주 바디 저장
    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO CONTRACTDETAIL (
            contract_cd, product_cd, product_sz, product_cr, 
            product_am, contract_ct, contract_ed, product_un
        ) VALUES (
            :#{#contractDetail.contract_cd}, :#{#contractDetail.product_cd}, 
            :#{#contractDetail.product_sz}, :#{#contractDetail.product_cr}, 
            :#{#contractDetail.product_am}, :#{#contractDetail.contract_ct}, 
            :#{#contractDetail.contract_ed}, :#{#contractDetail.product_un}
        )
    """, nativeQuery = true)
    void saveContractDetail(@Param("contractDetail") ContractDetail contractDetail);
	
	// 제품 조회
	@Query(value = """
		    SELECT 
		        p.product_cd,
		        p.product_nm,
		        p.product_cr,
		        p.product_sz,
		        p.product_un
		    FROM PRODUCT p
	       WHERE product_gc = 'PRODUCT'
			 AND product_cc = 'SHOES'
		ORDER BY product_cd ASC, product_cr ASC, product_sz ASC, product_un ASC
		""", nativeQuery = true)
	List<Object[]> select_RPODUCT();

	// 제품 검색
	@Query(value = """
		    SELECT 
		        p.product_cd,
		        p.product_nm,
		        p.product_cr,
		        p.product_sz,
		        p.product_un
		    FROM PRODUCT p
		    WHERE (p.product_cd LIKE %:keyword%
		        OR p.product_nm LIKE %:keyword%
		        OR p.product_cr LIKE %:keyword%
		        OR p.product_sz LIKE %:keyword%
		        OR p.product_un LIKE %:keyword%)
		       AND p.product_gc = 'PRODUCT' AND p.product_cc = 'SHOES'
		  ORDER BY product_cd ASC, product_cr ASC, product_sz ASC
		""", nativeQuery = true)
		List<Object[]> search_PRODUCT(@Param("keyword") String keyword);

	
	// 수주 거래처 조회
	@Query(value = """
		    SELECT 
		        a.account_cd AS account_cd,
		        a.account_nm AS account_nm
		    FROM ACCOUNT a
    		WHERE a.account_dv IN ('BOTH', 'CONTRACT')
	     ORDER BY account_cd
		""", nativeQuery = true)
	List<Object[]> select_ACCOUNT_CONTRACT();

	// 수주 거래처 검색
	@Query(value = """
		  SELECT 
		        a.account_cd AS account_cd,
		        a.account_nm AS account_nm
		    FROM ACCOUNT a
		   WHERE a.account_dv IN ('BOTH', 'CONTRACT')
    		 AND (a.account_cd LIKE %:keyword%
	          OR a.account_nm LIKE %:keyword%)
	    ORDER BY account_cd		        
		""", nativeQuery = true)
	List<Object[]> search_ACCOUNT_CONTRACT(@Param("keyword") String keyword);

	// 수주 담당자 조회
	@Query(value = """
		    SELECT 
		        e.employee_cd AS employee_cd,
		        e.employee_nm AS employee_nm,
		        c.common_nm AS employee_dp,
		        s.common_nm AS employee_gd
		    FROM EMPLOYEE e 
		    LEFT JOIN COMMON_CODE c 
		           ON e.employee_dp = c.common_cc 
		          AND c.common_gc = 'DEPARTMENT'
		    LEFT JOIN COMMON_CODE s 
		           ON e.employee_gd = s.common_cc 
		          AND s.common_gc = 'POSITION'
				WHERE c.common_nm = '영업'
		ORDER BY employee_cd		
		""", nativeQuery = true)
	List<Object[]> select_CONTRACT_PS();

	// 수주 담당자 검색
	@Query(value = """
		    SELECT 
		        e.employee_cd AS employee_cd,
		        e.employee_nm AS employee_nm,
		        c.common_nm AS employee_dp,
		        s.common_nm AS employee_gd
		    FROM EMPLOYEE e 
		    LEFT JOIN COMMON_CODE c 
		           ON e.employee_dp = c.common_cc 
		          AND c.common_gc = 'DEPARTMENT'
		    LEFT JOIN COMMON_CODE s 
		           ON e.employee_gd = s.common_cc 
		          AND s.common_gc = 'POSITION'
			WHERE (e.employee_cd LIKE %:keyword%
		        OR e.employee_nm LIKE %:keyword%
		        OR c.common_nm LIKE %:keyword%
		        OR s.common_nm LIKE %:keyword%)
			   AND c.common_nm = '영업'
		   ORDER BY employee_cd	
		""", nativeQuery = true)
	List<Object[]> search_CONTRACT_PS(@Param("keyword") String keyword);

	// 수주 조회
	@Query(value = """
		    SELECT
		        c.contract_cd,
		        c.account_cd,
		        c.employee_cd,
		        c.contract_ps,
		        c.contract_sd,
		        c.contract_ed,
		        c.contract_am,
		        c.contract_mn,
		        c.contract_st,
		        c.contract_rm,
		        c.contract_wr,
		        c.contract_wd,
		        c.contract_mf,
		        c.contract_md
		    FROM CONTRACT c
		    ORDER BY contract_cd DESC
		""", nativeQuery = true)
	List<Object[]> select_CONTRACT();
	
	// 출하페이지 수주조회
	@Query(value = """
		SELECT * FROM (
		    SELECT
		        c.contract_cd,
		        c.account_cd,
		        c.employee_cd,
		        c.contract_ps,
		        c.contract_sd,
		        c.contract_ed,
		        c.contract_am,
		        c.contract_mn,
				c.contract_st,
		        c.contract_rm,
		        c.contract_wr,
		        c.contract_wd,
		        c.contract_mf,
		        c.contract_md
		    FROM CONTRACT c
		) sub
		WHERE contract_st NOT IN ('대기중')
		ORDER BY contract_cd DESC
		""", nativeQuery = true)
	List<Object[]> select_CONTRACT_SHIPMENT();

	// 수주상세 조회
	@Query(value = """
			SELECT d.contract_cd, 
			       d.product_cd, 
			       d.product_sz, 
			       d.product_cr,  
			       d.product_am,
			       d.contract_ct,
			       d.product_un, 
			       d.contract_ed AS detail_contract_ed
			FROM CONTRACTDETAIL d
			WHERE d.contract_cd = :contractCd
			ORDER BY contract_cd DESC
		""", nativeQuery = true)
	List<Object[]> select_CONTRACTDETAIL(@Param("contractCd") String contractCd);

	// 출하 바디 조회
	@Query(value = """
		SELECT d.contract_cd, 
		       d.product_cd, 
		       d.product_sz, 
		       d.product_cr,  
		       d.product_am,
		       d.contract_ct,
		       d.product_un, 
		       d.contract_ed AS detail_contract_ed,
		       COALESCE(s.total_stock, 0) AS total_stock,
		       CASE 
		           WHEN COALESCE(s.total_stock, 0) >= d.product_am THEN '출하가능'
		           ELSE '재고부족 (' || (d.product_am - COALESCE(s.total_stock, 0)) || '개)'
		       END AS can_ship
		FROM CONTRACTDETAIL d
		LEFT JOIN (
		    SELECT 
		        item_cd, 
		        SUM(stock_aq) AS total_stock
		    FROM STOCK 
		    GROUP BY item_cd
		) s 
		ON (d.product_cd || '-' || d.product_sz || '-' || d.product_cr) = s.item_cd
		WHERE d.contract_cd = :contractCd
		ORDER BY d.contract_cd DESC
		""", nativeQuery = true)
	List<Object[]> select_CONTRACTDETAIL_SHIPMENT(@Param("contractCd") String contractCd);
	
	// 수주 헤드 가져오기
    @Query(value = """
            SELECT
                c.contract_cd,
                c.account_cd,
                c.employee_cd,
                c.contract_ps,
                c.contract_sd,
                c.contract_ed,
                c.contract_am,
                c.contract_mn,
                c.contract_st,
                c.contract_rm,
                c.contract_wr,
                c.contract_wd,
                c.contract_mf,
                c.contract_md
            FROM CONTRACT c
            WHERE c.contract_cd = :contractCd
            ORDER BY contract_cd DESC
        """, nativeQuery = true)
    List<Object[]> findContractByCd(@Param("contractCd") String contractCd);

    // 출하 헤드 조회
    @Query(value = """
            SELECT
                c.contract_cd,
                c.account_cd,
                c.contract_sd,
                c.contract_ed,
                c.contract_am,
                c.contract_st
            FROM CONTRACT c
            WHERE c.contract_cd = :contractCd
            ORDER BY contract_cd DESC
        """, nativeQuery = true)
    List<Object[]> findContractShipmentByCd(@Param("contractCd") String contractCd);
	
    // 기존 수주 바디 삭제
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CONTRACTDETAIL WHERE contract_cd = :contractCd", nativeQuery = true)
    void deleteContractDetailsByContractCd(@Param("contractCd") String contractCd);

    // 수주 헤드 삭제
    @Modifying
    @Query(value = """
    		DELETE
    		  FROM 
    		  	CONTRACT c 
		  	 WHERE 
		  	 	c.contract_cd IN :contractCds 
	  	 	   AND c.contract_st = '대기중'
		""", nativeQuery = true)
    int delete_CONTRACT(@Param("contractCds") List<String> contractCds);

    // 수주 바디 삭제
    @Modifying
    @Query(value = """
    		DELETE
    		  FROM 
    		  	CONTRACTDETAIL c 
		  	 WHERE 
		  	 	c.contract_cd IN :contractCds 
		""", nativeQuery = true)
    int delete_CONTRACTDETAIL(@Param("contractCds") List<String> contractCds);
	
    // 수주 필터 데이터 가져오기
    @Query(value = """
			SELECT
                c.contract_cd,
                c.account_cd,
                c.employee_cd,
                c.contract_ps,
                c.contract_sd,
                c.contract_ed,
                c.contract_am,
                c.contract_mn,
                c.contract_st,
                c.contract_rm,
                c.contract_wr,
                c.contract_wd,
                c.contract_mf,
                c.contract_md
            FROM CONTRACT c
			WHERE (:#{#filterRequest.contractCd} IS NULL OR c.contract_cd LIKE %:#{#filterRequest.contractCd}%) 
			  AND (:#{#filterRequest.accountCd} IS NULL OR c.account_cd LIKE %:#{#filterRequest.accountCd}%)
			  AND (:#{#filterRequest.contractPs} IS NULL OR c.contract_ps LIKE %:#{#filterRequest.contractPs}%)
			  AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL
			   OR c.contract_sd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate})
		   ORDER BY contract_cd DESC
        """, nativeQuery = true)
    List<Object[]> select_FILTERED_CONTRACT(@Param("filterRequest") ContractFilterRequest filterRequest);

    // 출하 필터 데이터 가져오기
    @Query(value = """
			SELECT
                c.contract_cd,
                c.account_cd,
                c.employee_cd,
                c.contract_ps,
                c.contract_sd,
                c.contract_ed,
                c.contract_am,
                c.contract_mn,
                c.contract_st,
                c.contract_rm,
                c.contract_wr,
                c.contract_wd,
                c.contract_mf,
                c.contract_md
            FROM CONTRACT c
			WHERE (:#{#filterRequest.contractCd} IS NULL OR c.contract_cd LIKE %:#{#filterRequest.contractCd}%) 
			  AND (:#{#filterRequest.accountCd} IS NULL OR c.account_cd LIKE %:#{#filterRequest.accountCd}%)
			  AND (:#{#filterRequest.contractPs} IS NULL OR c.contract_ps LIKE %:#{#filterRequest.contractPs}%)
			  AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL
			   OR c.contract_sd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate})
		      AND c.contract_st NOT IN ('대기중')
		   ORDER BY contract_cd DESC
        """, nativeQuery = true)
    List<Object[]> select_FILTERED_CONTRACT_SHIPMENT(@Param("filterRequest") ContractFilterRequest filterRequest);
    
    
    // 수주 상태 업데이트
    @Modifying
    @Transactional
    @Query(value = """
        UPDATE CONTRACT c
        SET c.contract_st = 
            CASE 
		        WHEN c.contract_sd > SYSDATE THEN '대기중'
		        WHEN c.contract_sd <= SYSDATE AND (c.contract_ed IS NULL OR c.contract_ed > SYSDATE) THEN '진행중'
		        WHEN c.contract_sd <= SYSDATE AND c.contract_ed <= SYSDATE THEN '완료'
		        ELSE '알 수 없음'
            END
    """, nativeQuery = true)
    void updateContractStatus();
    
    // contract 출하 등록 시 테이블에 출하일자 및 상태 변경처리
    @Modifying
    @Transactional
    @Query(value = """
		UPDATE CONTRACT SET contract_ed = :contractEd, contract_st = :contractSt
               WHERE contract_cd = :contractCd
	""", nativeQuery = true)
    void updateContractShipment(@Param("contractEd") String contractEd,  @Param("contractSt") String contractSt, @Param("contractCd") String contractCd);
    
    // contractDetail 테이블 출하 등록 시 출하일자 변경 처리
    @Modifying
    @Transactional
    @Query(value = """
		UPDATE CONTRACTDETAIL
               SET contract_ed = :contractEd 
               WHERE contract_cd = :contractCd 
   	""", nativeQuery = true)
    void updateContractDetailShipment(@Param("contractEd") String contractEd, @Param("contractCd") String contractCd);

    // 재고가 있는 제품 창고 찾기
    @Query(value = """
	    SELECT warehouse_cd, warearea_cd, stock_aq
	    FROM STOCK
	    WHERE item_cd = :itemCd
	      AND stock_aq > 0
	    ORDER BY stock_aq DESC
   	""", nativeQuery = true)
    List<Map<String, Object>> findAvailableStockByItem(@Param("itemCd") String itemCd);

    // 총 제품 재고량
    @Query(value = """
    		SELECT SUM(stock_aq) FROM STOCK WHERE item_cd = :itemCd
	""", nativeQuery = true)
    Integer getTotalStock(@Param("itemCd") String itemCd);

    
    // 출고 처리 
    @Modifying
    @Transactional
    @Query(value = """
		    UPDATE STOCK
		    SET stock_aq = stock_aq - :shipmentQty,
		        stock_uu = :updatedUser,
		        stock_ud = CURRENT_TIMESTAMP
		    WHERE ROWID IN (
		        SELECT ROWID FROM STOCK 
		        WHERE item_cd = :itemCd
		          AND warehouse_cd = :warehouseCd
		          AND warearea_cd = :wareareaCd
		          AND stock_aq > 0
		        ORDER BY stock_rd ASC
		        FETCH FIRST 1 ROWS ONLY
		    )
		    AND (SELECT SUM(s.stock_aq) FROM STOCK s WHERE s.item_cd = :itemCd) >= :shipmentQty
           """, nativeQuery = true)
    int reduceStockQuantity(@Param("itemCd") String itemCd,
                            @Param("warehouseCd") String warehouseCd,
                            @Param("wareareaCd") String wareareaCd,
                            @Param("shipmentQty") int shipmentQty,
                            @Param("updatedUser") String updatedUser);
    
    
    
	// 수주 현황 조회
    @Query(value = """
    	    SELECT
    	        c.contract_cd,
    	        c.account_cd,
    	        c.contract_sd,
    	        c.contract_ed,
    	        c.contract_am,
    	        c.contract_mn
    	    FROM CONTRACT c
    	    WHERE c.contract_sd BETWEEN :startDt AND :endDt
    	    AND (c.contract_ed IS NULL OR c.contract_ed >= :startDt)
    	    ORDER BY c.contract_cd DESC
    	""", nativeQuery = true)
	List<Object[]> select_CONTRACT_STATE(@Param("startDt") String startDt, @Param("endDt") String endDt);

	
	// 출하 현황 조회
	@Query(value = """
    	    SELECT
    	        c.contract_cd,
    	        c.account_cd,
    	        c.contract_sd,
    	        c.contract_ed,
    	        c.contract_am,
    	        c.contract_mn
    	    FROM CONTRACT c
    	    WHERE c.contract_sd BETWEEN :startDt AND :endDt
    	    AND (c.contract_ed IS NULL OR c.contract_ed >= :startDt)
    	    AND c.contract_st NOT IN ('대기중')
    	    ORDER BY c.contract_cd DESC
    	""", nativeQuery = true)
	List<Object[]> select_SHIPMENT_STATE(@Param("startDt") String startDt, @Param("endDt") String endDt);
    
}