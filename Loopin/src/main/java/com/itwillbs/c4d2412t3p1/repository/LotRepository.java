package com.itwillbs.c4d2412t3p1.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Lot;
import com.itwillbs.c4d2412t3p1.entity.LotPK;

@Repository
public interface LotRepository extends JpaRepository<Lot, LotPK> {
	
}