package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.PRCode;

@Repository
public interface PRCodeRepository extends JpaRepository<PRCode, String> {
	
	@Query(value="SELECT prcode_id, prcode_nm, prcode_nt, prcode_fl, prcode_gy FROM PRCode WHERE prcode_id Like ?1%", nativeQuery=true)
	List<PRCode> findByPrcodeIdStartingWith(@Param("String B") String B);

}
