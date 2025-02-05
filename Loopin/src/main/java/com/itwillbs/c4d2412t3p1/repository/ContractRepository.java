package com.itwillbs.c4d2412t3p1.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Account;
import com.itwillbs.c4d2412t3p1.entity.Contract;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.AccountFilterRequest;

import jakarta.transaction.Transactional;

@Repository
public interface ContractRepository  extends JpaRepository<Contract, String> {

	@Query(value = """
		    SELECT 
		        p.product_cd AS product_cd,
		        p.product_nm AS product_nm,
		        c.common_nm AS product_cr,
		        s.common_nm AS product_sz
		    FROM PRODUCT p
		    LEFT JOIN COMMON_CODE c 
		           ON p.product_cr = c.common_cc 
		          AND c.common_gc = 'COLOR'
		    LEFT JOIN COMMON_CODE s 
		           ON p.product_sz = s.common_cc 
		          AND s.common_gc = 'SIZE'
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
        
        
	    
}