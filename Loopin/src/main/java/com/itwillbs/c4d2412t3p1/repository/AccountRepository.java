package com.itwillbs.c4d2412t3p1.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.c4d2412t3p1.entity.Account;

@Repository
public interface AccountRepository  extends JpaRepository<Account, String> {

	@Query(value = "SELECT a.account_cd AS account_cd, \n" +
            "       a.account_nm AS account_nm, \n" +
            "       d.common_nm AS account_dv, \n" +
            "       a.account_cp AS account_cp, \n" +
            "       a.account_ps AS account_ps, \n" +
            "       a.account_ph AS account_ph, \n" +
            "       a.account_fx AS account_fx, \n" +
            "       a.account_em AS account_em, \n" +
            "       a.account_sb AS account_sb, \n" +
            "       q.common_nm AS account_uj, \n" +
            "       t.common_nm AS account_ut, \n" +
            "       a.account_ad AS account_ad, \n" +
            "       a.account_sd AS account_sd, \n" +
            "       a.account_ed AS account_ed, \n" +
            "       a.account_bk AS account_bk, \n" +
            "       a.account_an AS account_an, \n" +
            "       a.account_dt AS account_dt, \n" +
            "       p.common_nm AS account_us, \n" +
            "       a.account_nt AS account_nt, \n" +
            "       a.account_wr AS account_wr, \n" +
            "       a.account_wd AS account_wd, \n" +
            "       a.account_mf AS account_mf, \n" +
            "       a.account_md AS account_md \n" + 
            "FROM ACCOUNT a \n" +
            "LEFT JOIN common_code d ON a.account_dv = d.common_cc AND d.common_gc = 'ACTYPE' \n" +
            "LEFT JOIN common_code q ON a.account_uj = q.common_cc AND q.common_gc = 'BITYPE' \n" +
            "LEFT JOIN common_code t ON a.account_ut = t.common_cc AND t.common_gc = 'BTTYPE' \n" +
            "LEFT JOIN common_code p ON a.account_us = p.common_cc AND p.common_gc = 'USEYN' \n",
    nativeQuery = true)
List<Object[]> findAllWithDetails();

	
	
	
}