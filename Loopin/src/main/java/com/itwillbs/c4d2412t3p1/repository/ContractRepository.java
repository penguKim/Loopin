package com.itwillbs.c4d2412t3p1.repository;


import java.util.List;

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
	
	
	@Query(value = """
		    SELECT 
		        p.product_cd AS product_cd,
		        p.product_nm AS product_nm,
		        c.common_nm AS product_cr,
		        s.common_nm AS product_sz,
		        u.common_nm AS product_un
		    FROM PRODUCT p
		    LEFT JOIN COMMON_CODE c 
		           ON p.product_cr = c.common_cc 
		          AND c.common_gc = 'COLOR'
		    LEFT JOIN COMMON_CODE s 
		           ON p.product_sz = s.common_cc 
		          AND s.common_gc = 'SIZE'
		    LEFT JOIN COMMON_CODE u 
		           ON p.product_un = u.common_cc 
		          AND u.common_gc = 'UNIT'
	       WHERE product_gc = 'PRODUCT'
			 AND product_cc = 'SHOES'
		ORDER BY product_cd ASC, product_cr ASC, product_sz ASC, product_un ASC
		""", nativeQuery = true)
	List<Object[]> select_RPODUCT();

	
	@Query(value = """
		    SELECT p.product_cd, p.product_nm, c.common_nm AS product_cr,
		           s.common_nm AS product_sz, u.common_nm AS product_un
		    FROM PRODUCT p
		    LEFT JOIN COMMON_CODE c ON p.product_cr = c.common_cc AND c.common_gc = 'COLOR'
		    LEFT JOIN COMMON_CODE s ON p.product_sz = s.common_cc AND s.common_gc = 'SIZE'
		    LEFT JOIN COMMON_CODE u ON p.product_un = u.common_cc AND u.common_gc = 'UNIT'
		    WHERE (p.product_cd LIKE %:keyword%
		        OR p.product_nm LIKE %:keyword%
		        OR c.common_nm LIKE %:keyword%
		        OR s.common_nm LIKE %:keyword%
		        OR u.common_nm LIKE %:keyword%)
		       AND p.product_gc = 'PRODUCT' AND p.product_cc = 'SHOES'
		  ORDER BY product_cd ASC, product_cr ASC, product_sz ASC
		""", nativeQuery = true)
		List<Object[]> search_PRODUCT(@Param("keyword") String keyword);

	
	
	@Query(value = """
		    SELECT 
		        a.account_cd AS account_cd,
		        a.account_nm AS account_nm
		    FROM ACCOUNT a
    		WHERE a.account_dv IN ('BOTH', 'CONTRACT')
	     ORDER BY account_cd ASC
		""", nativeQuery = true)
	List<Object[]> select_ACCOUNT_CONTRACT();

	@Query(value = """
		  SELECT 
		        a.account_cd AS account_cd,
		        a.account_nm AS account_nm
		    FROM ACCOUNT a
		   WHERE a.account_dv IN ('BOTH', 'CONTRACT')
    		 AND (a.account_cd LIKE %:keyword%
	          OR a.account_nm LIKE %:keyword%)
	    ORDER BY account_cd ASC		        
		""", nativeQuery = true)
	List<Object[]> search_ACCOUNT_CONTRACT(@Param("keyword") String keyword);

	
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
		ORDER BY employee_cd ASC		
		""", nativeQuery = true)
	List<Object[]> select_CONTRACT_PS();

	
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
		   ORDER BY employee_cd ASC	
		""", nativeQuery = true)
	List<Object[]> search_CONTRACT_PS(@Param("keyword") String keyword);

	
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
		        CASE 
		            WHEN c.contract_sd > SYSDATE THEN '대기중'
		            WHEN c.contract_sd <= SYSDATE AND (c.contract_ed IS NULL OR c.contract_ed >= SYSDATE) THEN '진행중'
		            WHEN c.contract_sd <= SYSDATE AND c.contract_ed < SYSDATE THEN '완료'
		            ELSE '알 수 없음'
		        END AS contract_st,
		        c.contract_rm,
		        c.contract_wr,
		        c.contract_wd,
		        c.contract_mf,
		        c.contract_md
		    FROM CONTRACT c
		    ORDER BY contract_cd ASC	
		""", nativeQuery = true)
	List<Object[]> select_CONTRACT();

	
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
			ORDER BY contract_cd ASC
		""", nativeQuery = true)
	List<Object[]> select_CONTRACTDETAIL(@Param("contractCd") String contractCd);
	
	
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
            ORDER BY contract_cd ASC
        """, nativeQuery = true)
    List<Object[]> findContractByCd(@Param("contractCd") String contractCd);
	
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CONTRACTDETAIL WHERE contract_cd = :contractCd", nativeQuery = true)
    void deleteContractDetailsByContractCd(@Param("contractCd") String contractCd);

    
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

    @Modifying
    @Query(value = """
    		DELETE
    		  FROM 
    		  	CONTRACTDETAIL c 
		  	 WHERE 
		  	 	c.contract_cd IN :contractCds 
		""", nativeQuery = true)
    int delete_CONTRACTDETAIL(@Param("contractCds") List<String> contractCds);
	
    
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
		   ORDER BY contract_cd ASC
        """, nativeQuery = true)
    List<Object[]> select_FILTERED_CONTRACT(@Param("filterRequest") ContractFilterRequest filterRequest);
    
    
    // 수주 상태 업데이트
    @Modifying
    @Transactional
    @Query(value = """
        UPDATE CONTRACT c
        SET c.contract_st = 
            CASE 
                WHEN c.contract_sd > SYSDATE THEN '대기중'
                WHEN c.contract_sd <= SYSDATE AND (c.contract_ed IS NULL OR c.contract_ed >= SYSDATE) THEN '진행중'
                WHEN c.contract_sd <= SYSDATE AND c.contract_ed < SYSDATE THEN '완료'
                ELSE '알 수 없음'
            END
    """, nativeQuery = true)
    void updateContractStatus();
    
	
}