package com.itwillbs.c4d2412t3p1.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Productplan;
import com.itwillbs.c4d2412t3p1.entity.ProductplanPK;

@Repository
public interface ProductplanRepository extends JpaRepository<Productplan, ProductplanPK>{
}
