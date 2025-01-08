package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.Workinghour;
import com.itwillbs.c4d2412t3p1.entity.common_codePK;
import com.itwillbs.c4d2412t3p1.mapper.CommonMapper;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.CommuteRepository;
import com.itwillbs.c4d2412t3p1.repository.WokinghourRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class CommuteService {
	
	private final CommonRepository commonRepository;
	private final CommuteRepository commuteRepository;
	private final WokinghourRepository wokinghourRepository;
	
	


    public List<Commute> select_COMMUTE(String commute_dt) {
    	
        if (commute_dt == null || commute_dt.isEmpty()) {
            return commuteRepository.findAll();
        } else {
            return commuteRepository.select_COMMUTE(commute_dt);
        }
    }



	public List<CommuteDTO> select_COMMUTE_list() {
		return commuteRepository.select_COMMUTE_list();
	}


	public List<Workinghour> select_WORKINGHOUR() {
		
		return wokinghourRepository.findAll();
	}
	
	public List<Common_codeDTO> select_COMMON_list(String common_gc) {
		
		return commonRepository.select_COMMON_list(common_gc);
	}
	

}
