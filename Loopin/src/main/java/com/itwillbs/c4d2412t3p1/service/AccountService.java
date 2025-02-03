package com.itwillbs.c4d2412t3p1.service;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.AccountDTO;
import com.itwillbs.c4d2412t3p1.entity.Account;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.repository.AccountRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;

import jakarta.transaction.Transactional;
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
    
    // 거래처 데이터 조회
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

	
	// 거래처 등록
	@Transactional
    public void insert_ACCOUNT(AccountDTO accountDTO) throws IOException {
        
        // 시퀀스 값 가져오기
        Long sequenceValue = accountRepository.getNextSequenceValue();
        System.out.println("Next Sequence Value: " + sequenceValue);

        Account account = Account.createAccount(accountDTO, sequenceValue);
        
        accountRepository.save(account);
        System.out.println("@@@@@@@@@" + account);
    }
	
	
	public void update_ACCOUNT(AccountDTO accountDTO) throws IOException {
	    Account account = accountRepository.findById(accountDTO.getAccount_cd())
	            .orElseThrow(() -> new IllegalArgumentException("해당 거래처가 존재하지 않습니다."));
	    
	    // 엔티티 업데이트
	    account.setAccountEntity(account, accountDTO);

	    // 데이터베이스 저장
	    accountRepository.save(account);
		
	}
	
	
    // CD로 Account 조회
    public Account findNoticeById(String account_cd) {
        // Repository를 사용하여 데이터 조회
        return accountRepository.findById(account_cd)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 데이터를 찾을 수 없습니다."));
    }
	
	
}
