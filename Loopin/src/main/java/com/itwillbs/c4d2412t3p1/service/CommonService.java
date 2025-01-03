package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.mapper.CommonMapper;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class CommonService {
	
	private final CommonMapper commonMapper;
	
	private final CommonRepository commonRepository;
	
	
	
	public List<Common_codeDTO> SELECT_COMMON_CODE(String code) {
		List<Common_codeDTO> list = commonMapper.SELECT_COMMON_CODE(code);
		
		return list;
	}



	public int save(List<Common_codeDTO> createdRows, String code) {
		String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
		int count = 0;
		for (Common_codeDTO data : createdRows) {
			
			if(code == null || code.equals("")) {
				data.setCommon_gc("00");
			} else {
				data.setCommon_gc(code);
			}

			data.setCommon_ct("");
			data.setCommon_us("true");
			data.setCommon_ru(regUser);
			data.setCommon_rd(new Timestamp(System.currentTimeMillis()));
//			Common_code common = commonRepository.save(Common_code.Common_code(data));
			
			int result = commonMapper.insertCommonCode(data) == 1 ? count++ : count;
			count += result;
			
		}
		return count;
	}




	public int delete_group_code(List<Common_codeDTO> list) {

		return commonMapper.delete_group_code(list);
		
	}
	

}
