package com.itwillbs.c4d2412t3p1.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.repository.AccountRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class AccountService {
  
	private final AccountRepository accountRepository;
	
	private final CommonRepository commonRepository;
	
    // 공통코드 데이터 조회
    public List<Common_code> selectCommonList(String string) {
    	return commonRepository.selectCommonList("00", string);
    }
    
	public List<Map<String, Object>> select_ACCOUNT_DETAIL() {

		List<Object[]> result;
		
		result = accountRepository.findAllWithDetails();
		
		return result.stream().map(row -> {
			Map<String, Object> account = new HashMap<>();
			account.put("account_cd", row[0]);
			account.put("account_nm", row[1]);
			account.put("account_dv", row[2]);
			account.put("account_cp", row[3]);
			account.put("account_ps", row[4]);
			account.put("account_ph", row[5]);
			account.put("account_fx", row[6]);
			account.put("account_em", row[7]);
			account.put("account_sb", row[8]);
			account.put("account_uj", row[9]);
			account.put("account_ut", row[10]);
			account.put("account_ad", row[11]);
			account.put("account_sd", row[12]);
			account.put("account_ed", row[13]);
			account.put("account_bk", row[14]);
			account.put("account_an", row[15]);
			account.put("account_dt", row[16]);
			account.put("account_us", row[17]);
			account.put("account_nt", row[18]);
			account.put("account_wr", row[19]);
			account.put("account_wd", row[20]);
			account.put("account_mf", row[21]);
			account.put("account_md", row[22]);


			return account;
			
		}).collect(Collectors.toList());
	}

	
}
