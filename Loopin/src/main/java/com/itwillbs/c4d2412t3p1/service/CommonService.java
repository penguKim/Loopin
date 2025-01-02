package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.mapper.CommonMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class CommonService {
	
	private final CommonMapper commonMapper;
	
//	private final CommonRepository commonRepository;
	
	
	
	public List<Common_codeDTO> SELECT_COMMON_CODE(String code) {
		List<Common_codeDTO> list = commonMapper.SELECT_COMMON_CODE(code);
		
		return list;
	}



	public void save(List<Common_codeDTO> createdRows, String code) {
		String regUser = SecurityContextHolder.getContext().getAuthentication().getName();
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
		}
		
		commonMapper.insertCommonCodes(createdRows);
		
	}



	public void delete_group_code(List<Map<String, Object>> list) {

		commonMapper.delete_group_code(list);
		
	}
	

}
