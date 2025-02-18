package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Bom;
import com.itwillbs.c4d2412t3p1.entity.BomId;

@Repository
public interface BomRepository extends JpaRepository<Bom, BomId> {

}
