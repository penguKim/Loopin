package com.itwillbs.c4d2412t3p1.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Account;
import com.itwillbs.c4d2412t3p1.entity.Contract;
import com.itwillbs.c4d2412t3p1.entity.ContractDetail;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.AccountFilterRequest;

import jakarta.transaction.Transactional;

@Repository
public interface ContractRepository  extends JpaRepository<Contract, String> {

	// 시퀀스 조회
	@Query(value = "SELECT NT_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
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
		""", nativeQuery = true)
	List<Object[]> select_RPODUCT();

	
	@Query(value = """
		    SELECT 
		        a.account_cd AS account_cd,
		        a.account_nm AS account_nm
		    FROM ACCOUNT a
		""", nativeQuery = true)
	List<Object[]> select_ACCOUNT_CONTRACT();

	
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
		""", nativeQuery = true)
	List<Object[]> select_CONTRACT_PS();

	
	@Query(value = """
		    SELECT
				c.contract_cd,
				c.account_cd,
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
		""", nativeQuery = true)
	List<Object[]> select_CONTRACTDETAIL(@Param("contractCd") String contractCd);
	
	
    @Query(value = """
            SELECT
                c.contract_cd,
                c.account_cd,
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
        """, nativeQuery = true)
    List<Object[]> findContractByCd(@Param("contractCd") String contractCd);
	
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM CONTRACTDETAIL WHERE contract_cd = :contractCd", nativeQuery = true)
    void deleteContractDetailsByContractCd(@Param("contractCd") String contractCd);

	
	
}