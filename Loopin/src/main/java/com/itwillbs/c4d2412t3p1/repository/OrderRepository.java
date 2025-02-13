package com.itwillbs.c4d2412t3p1.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.domain.OrderDetailDTO;
import com.itwillbs.c4d2412t3p1.entity.Order;
import com.itwillbs.c4d2412t3p1.entity.OrderDetail;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.OrderFilterRequest;

import jakarta.transaction.Transactional;

@Repository
public interface OrderRepository  extends JpaRepository<Order, String> {

	// 시퀀스 조회
	@Query(value = "SELECT OD_SEQUENCE.NEXTVAL FROM DUAL", nativeQuery = true)
	Long getNextSequenceValue();
	
    // 발주 바디 저장
    @Transactional
    @Modifying
    @Query(value = """
        INSERT INTO ORDERDETAIL (
            order_cd, material_cd, material_am, order_ct, order_ed, material_un
        ) VALUES (
            :#{#orderDetail.order_cd}, :#{#orderDetail.material_cd}, 
            :#{#orderDetail.material_am}, :#{#orderDetail.order_ct}, 
            :#{#orderDetail.order_ed}, :#{#orderDetail.material_un}
        )
    """, nativeQuery = true)
    void saveOrderDetail(@Param("orderDetail") OrderDetail orderDetail);
	
	
	@Query(value = """
		    SELECT 
		        m.material_cd AS material_cd,
		        m.material_nm AS material_nm,
		        u.common_nm AS material_un
		    FROM MATERIAL m
		    LEFT JOIN COMMON_CODE u 
		           ON m.material_un = u.common_cc 
		          AND u.common_gc = 'UNIT'
		""", nativeQuery = true)
	List<Object[]> select_MATERIAL();

	@Query(value = """
		    SELECT 
		        m.material_cd AS material_cd,
		        m.material_nm AS material_nm,
		        u.common_nm AS material_un
		    FROM MATERIAL m
		    LEFT JOIN COMMON_CODE u 
		           ON m.material_un = u.common_cc
		          AND u.common_gc = 'UNIT'
		    WHERE (m.material_cd LIKE %:keyword%
		        OR m.material_nm LIKE %:keyword%
		        OR u.common_nm LIKE %:keyword%)
		""", nativeQuery = true)
	List<Object[]> search_MATERIAL(@Param("keyword") String keyword);
	
	
	
	
	@Query(value = """
		SELECT 
		    a.account_cd AS account_cd,
		    a.account_nm AS account_nm
		FROM ACCOUNT a
		WHERE a.account_dv IN ('BOTH', 'ORDER')
		""", nativeQuery = true)
	List<Object[]> select_ACCOUNT_ORDER();

	@Query(value = """
		    SELECT 
		        a.account_cd AS account_cd,
		        a.account_nm AS account_nm
		    FROM ACCOUNT a
    		WHERE a.account_dv IN ('BOTH', 'CONTRACT')
    		 AND (a.account_cd LIKE %:keyword%
		        OR a.account_nm LIKE %:keyword%)
		""", nativeQuery = true)
	List<Object[]> search_ACCOUNT_ORDER(@Param("keyword") String keyword);
	
	
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
	List<Object[]> select_ORDER_PS();

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
		""", nativeQuery = true)
	List<Object[]> search_ORDER_PS(@Param("keyword") String keyword);
	
	
	@Query(value = """
		    SELECT
				o.order_cd,
				o.account_cd,
				o.order_ps,
				o.order_sd,
				o.order_ed,
				o.order_am,
				o.order_mn,
		       CASE 
		            WHEN o.order_sd > SYSDATE THEN '대기중'
		            WHEN o.order_sd <= SYSDATE AND (o.order_ed IS NULL OR o.order_ed >= SYSDATE) THEN '진행중'
		            WHEN o.order_sd <= SYSDATE AND o.order_ed < SYSDATE THEN '완료'
		            ELSE '알 수 없음'
		        END AS order_st,
				o.order_rm,
				o.order_wr,
				o.order_wd,
				o.order_mf,
				o.order_md
			FROM ORDERS o
		""", nativeQuery = true)
	List<Object[]> select_ORDER();

	
	@Query(value = """
			SELECT d.order_cd, 
			       d.material_cd, 
			       d.material_am, 
			       d.order_ct,  
			       d.material_un,
			       d.order_ed AS detail_order_ed
			FROM ORDERDETAIL d
			WHERE d.order_cd = :orderCd
		""", nativeQuery = true)
	List<Object[]> select_ORDERDETAIL(@Param("orderCd") String orderCd);
	
	
    @Query(value = """
            SELECT
				o.order_cd,
				o.account_cd,
				o.order_ps,
				o.order_sd,
				o.order_ed,
				o.order_am,
				o.order_mn,
				o.order_st,
				o.order_rm,
				o.order_wr,
				o.order_wd,
				o.order_mf,
				o.order_md
            FROM ORDERS o
            WHERE o.order_cd = :orderCd
        """, nativeQuery = true)
    List<Object[]> findOrderByCd(@Param("orderCd") String orderCd);
	
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM ORDERDETAIL WHERE order_cd = :orderCd", nativeQuery = true)
    void deleteOrderDetailsByOrderCd(@Param("orderCd") String orderCd);

    @Modifying
    @Query(value = """
    		DELETE
    		  FROM 
    		  	ORDERS o
		  	 WHERE 
		  	 	o.order_cd IN :orderCds 
	  	 	   AND o.order_st = '대기중'
		""", nativeQuery = true)
    int delete_ORDER(@Param("orderCds") List<String> orderCds);

    @Modifying
    @Query(value = """
    		DELETE
    		  FROM 
    		  	ORDERDETAIL o 
		  	 WHERE 
		  	 	o.order_cd IN :orderCds 
		""", nativeQuery = true)
    int delete_ORDERDETAIL(@Param("orderCds") List<String> orderCds);
	
    @Query(value = """
			SELECT
				o.order_cd,
				o.account_cd,
				o.order_ps,
				o.order_sd,
				o.order_ed,
				o.order_am,
				o.order_mn,
				o.order_st,
				o.order_rm,
				o.order_wr,
				o.order_wd,
				o.order_mf,
				o.order_md
            FROM ORDER o
			WHERE (:#{#filterRequest.orderCd} IS NULL OR o.order_cd LIKE %:#{#filterRequest.orderCd}%) 
			  AND (:#{#filterRequest.accountCd} IS NULL OR o.account_cd LIKE %:#{#filterRequest.accountCd}%)
			  AND (:#{#filterRequest.orderPs} IS NULL OR o.order_ps LIKE %:#{#filterRequest.orderPs}%)
			  AND (:#{#filterRequest.startDate} IS NULL OR :#{#filterRequest.endDate} IS NULL
			   OR o.order_sd BETWEEN :#{#filterRequest.startDate} AND :#{#filterRequest.endDate})
        """, nativeQuery = true)
    List<Object[]> select_FILTERED_ORDER(@Param("filterRequest") OrderFilterRequest filterRequest);
    
    
    // 수주 상태 업데이트
    @Modifying
    @Transactional
    @Query(value = """
        UPDATE ORDER o
        SET o.order_st = 
            CASE 
                WHEN o.order_sd > SYSDATE THEN '대기중'
                WHEN o.order_sd <= SYSDATE AND (o.order_ed IS NULL OR o.order_ed >= SYSDATE) THEN '진행중'
                WHEN o.order_sd <= SYSDATE AND o.order_ed < SYSDATE THEN '완료'
                ELSE '알 수 없음'
            END
    """, nativeQuery = true)
    void updateOrderStatus();
    
	
}