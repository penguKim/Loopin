package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Comhistory;
import com.itwillbs.c4d2412t3p1.entity.ComhistoryPK;


@Repository
public interface ComhistoryRepository extends JpaRepository<Comhistory, ComhistoryPK> {

}
