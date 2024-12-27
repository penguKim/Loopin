package com.itwillbs.c4d2412t3p1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

	Member findByIdAndPass(String id, String pass);
	
}
