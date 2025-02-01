package com.itwillbs.c4d2412t3p1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Account;

@Repository
public interface AccountRepository  extends JpaRepository<Account, String> {

	
}