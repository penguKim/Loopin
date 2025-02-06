package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Material;
import com.itwillbs.c4d2412t3p1.entity.MaterialPK;


@Repository
public interface MaterialRepository extends JpaRepository<Material, MaterialPK> {

}
