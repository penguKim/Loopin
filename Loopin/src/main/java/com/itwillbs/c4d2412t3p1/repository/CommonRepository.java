package com.itwillbs.c4d2412t3p1.repository;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.common_codePK;


@Repository
public interface CommonRepository extends JpaRepository<Common_code, common_codePK> {

	
    @Modifying
    @Query(value = "DELETE FROM COMMON_CODE WHERE common_gc = '00' AND common_cc = :code", nativeQuery = true)
    int deleteGroupCode(@Param("code") String code);

    @Modifying
    @Query(value = "DELETE FROM COMMON_CODE WHERE common_gc = :code", nativeQuery = true)
    int deleteCommonCodeList(@Param("code") String code);
    
    @Modifying
    @Query(value = "DELETE FROM COMMON_CODE WHERE common_gc = :groupCode AND common_cc = :subCode", nativeQuery = true)
    int deleteCommonCode(@Param("groupCode") String groupCode, @Param("subCode") String subCode);
    
    @Modifying
    @Query(value = "UPDATE COMMON_CODE SET "
            + "common_cc = :common_cc, common_nm = :common_nm, common_ct = :common_ct, common_in = :common_in, "
            + "common_us = :common_us, common_uu = :common_uu, common_ud = :common_ud "
            + "WHERE common_gc = :common_gc AND common_cc = :before_cc", nativeQuery = true)
    int updateCommonCode(
        @Param("common_gc") String common_gc,
        @Param("before_cc") String before_cc,
        @Param("common_cc") String common_cc,
        @Param("common_nm") String common_nm,
        @Param("common_ct") String common_ct,
        @Param("common_in") String common_in,
        @Param("common_us") String common_us,
        @Param("common_uu") String common_uu,
        @Param("common_ud") Timestamp common_ud
    );

    
    // 사원 부서 리스트 가져오기 
    @Query(value = "SELECT common_gc, common_cc, common_nm, common_ct, common_in, common_us, common_ru, common_rd, common_uu, common_ud FROM COMMON_CODE WHERE common_gc = :common_gc", nativeQuery = true)
    List<Common_code> selectDeptList(@Param("common_cc") String common_cc, @Param("common_gc") String common_gc);
    
    // 사원 직급 리스트 가져오기 
    @Query(value = "SELECT common_gc, common_cc, common_nm, common_ct, common_in, common_us, common_ru, common_rd, common_uu, common_ud FROM COMMON_CODE WHERE common_gc = :common_gc", nativeQuery = true)
	List<Common_code> selectGradeList(@Param("common_cc") String common_cc, @Param("common_gc") String common_gc);

	// 셀렉트 박스 부서장 유무 리스트 가져오기 
	@Query(value = "SELECT common_gc, common_cc, common_nm, common_ct, common_in, common_us, common_ru, common_rd, common_uu, common_ud FROM COMMON_CODE WHERE common_gc = :common_gc", nativeQuery = true)
	List<Common_code> selectDPTypeList(@Param("common_cc") String common_cc, @Param("common_gc") String common_gc);


}
