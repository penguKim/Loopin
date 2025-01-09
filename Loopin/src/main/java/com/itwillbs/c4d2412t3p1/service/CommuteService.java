package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.CommuteDTO;
import com.itwillbs.c4d2412t3p1.domain.WorkinghourDTO;
import com.itwillbs.c4d2412t3p1.domain.WorktypeDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Commute;
import com.itwillbs.c4d2412t3p1.entity.Workinghour;
import com.itwillbs.c4d2412t3p1.entity.common_codePK;
import com.itwillbs.c4d2412t3p1.mapper.CommonMapper;
import com.itwillbs.c4d2412t3p1.mapper.CommuteMapper;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.CommuteRepository;
import com.itwillbs.c4d2412t3p1.repository.WorkinghourRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class CommuteService {
	
	private final CommonRepository commonRepository;
	private final CommuteRepository commuteRepository;
	private final WorkinghourRepository workinghourRepository;
	
	private final CommuteMapper commuteMapper; 
	
	


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


	// 근로관리 그리드 조회
	public List<WorkinghourDTO> select_WORKINGHOUR() {
		
		return commuteMapper.select_WORKINGHOUR();
	}
	
	// 공통코드 조회
	public List<Common_codeDTO> select_COMMON_list(String common_gc) {
		
		return commonRepository.select_COMMON_list(common_gc);
	}

	// 근로관리 상세 항목 조회
	public Workinghour select_WORKINGHOUR_detail(String workinghour_id) {
		return workinghourRepository.findById(workinghour_id).orElseThrow(() -> 
		new IllegalArgumentException("해당 ID의 데이터를 찾을 수 없습니다."));
	}

	// 근로관리 항목 등록
	public Workinghour insert_WORKINGHOUR(WorkinghourDTO workinghourDTO) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    
	    Optional<Workinghour> presentWorkinghour = workinghourRepository.findById(workinghourDTO.getWorkinghour_id());
	    
	    if(presentWorkinghour.isPresent()) {
	    	Workinghour present = presentWorkinghour.get();
	        workinghourDTO.setWorkinghour_ru(present.getWorkinghour_ru());
	        workinghourDTO.setWorkinghour_rd(present.getWorkinghour_rd());
	        workinghourDTO.setWorkinghour_uu(regUser);
	        workinghourDTO.setWorkinghour_ud(time);
	    } else {
	        workinghourDTO.setWorkinghour_ru(regUser);
	        workinghourDTO.setWorkinghour_rd(time);
	    }
	    
		return workinghourRepository.save(Workinghour.setCommute(workinghourDTO));
	}






	// 사원추가 모달 - 선택안된 직원 조회
	public List<WorkinghourDTO> select_EMPLOYEE_WORKINGHOUR(String workinghour_id) {
		return commuteMapper.select_EMPLOYEE_WORKINGHOUR(workinghour_id);
	}

	// 사원추가 모달 - 선택된 직원 조회
	public List<WorkinghourDTO> select_EMPLOYEE_WORKINGHOUR_CHK(String workinghour_id) {
		return commuteMapper.select_EMPLOYEE_WORKINGHOUR_CHK(workinghour_id);
	}

	// 사원의 근로 등록
	@Transactional
	public int update_EMPLOYEE_WK(List<WorkinghourDTO> list, String workinghour_id) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
	    Timestamp time = new Timestamp(System.currentTimeMillis());
		int count = 0;
		try {
			count = 0;
			for(WorkinghourDTO row : list) {
				row.setWorkinghour_id(row.getWorkinghour_id() == null ? workinghour_id : "");
				row.setWorkinghour_uu(regUser);
				row.setWorkinghour_ud(time);
				int updcount = commuteMapper.update_EMPLOYEE_WK(row);
				System.out.println("UPDCOUNT : " + updcount);
				if(updcount > 0) {
					count++;				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}




	

}
