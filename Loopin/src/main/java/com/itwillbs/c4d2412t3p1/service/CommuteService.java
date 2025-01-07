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
import com.itwillbs.c4d2412t3p1.entity.common_codePK;
import com.itwillbs.c4d2412t3p1.mapper.CommonMapper;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.CommuteRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class CommuteService {
	
	private final CommuteRepository commuteRepository;
	
	
	private final CommonMapper commonMapper;
	
	private final CommonRepository commonRepository;
	
	
	
	public List<Common_codeDTO> select_common_code(String code) {
		List<Common_codeDTO> list = commonMapper.select_common_code(code);
		
		return list;
	}

	
	@Transactional
	public Map<String, Object> insert_common_code(List<Common_codeDTO> createdRows, List<Common_codeDTO> updatedRows, String code) {
	    String regUser = SecurityContextHolder.getContext().getAuthentication().getName();

	    List<Common_code> insertList = new ArrayList<>();
	    List<Common_code> updateList = new ArrayList<>();
	    List<Common_codeDTO> failedRows = new ArrayList<>();

	    Timestamp time = new Timestamp(System.currentTimeMillis());
	    
	    createdRows.forEach(data -> {
	        try {
	            Common_code common_code = Common_code.builder()
	                    .common_gc(code == null || code.equals("") ? "00" : code)
	                    .common_cc(data.getCommon_cc())
	                    .common_nm(data.getCommon_nm())
	                    .common_ct("")
	                    .common_in(data.getCommon_in())
	                    .common_us("true")
	                    .common_ru(regUser)
	                    .common_rd(time)
	                    .build();
	            insertList.add(common_code);
	        } catch (Exception e) {
	            failedRows.add(data);
	        }
	    });

	    int insertCount = 0;

	    try {
	    	insertCount = commonRepository.saveAll(insertList).size();
	    } catch (Exception e) {
	        throw new RuntimeException("무언가 오류가 났어요!!!!!!!!!" + e.getMessage(), e);
	        
	    }
	    
	    int updateCount = 0;
	    
	    for (Common_codeDTO data : updatedRows) {
	        try {
	            String beforeCommon_cc = data.getBeforeCommon_cc();
	            String common_cc = (beforeCommon_cc != null && !beforeCommon_cc.isEmpty())
	                    ? beforeCommon_cc : data.getCommon_cc();
	            
	            commonRepository.updateCommonCode(
	                data.getCommon_gc(),
	                common_cc,
	                data.getCommon_cc(),
	                data.getCommon_nm(),
	                data.getCommon_ct(),
	                data.getCommon_in(),
	                data.getCommon_us(),
	                regUser, 
	                time
	            );

	            updateCount++;
	        } catch (Exception e) {
	            failedRows.add(data);
	        }
	    }

	    
	    Map<String, Object> result = new HashMap<>();
	    result.put("insertCount", insertCount);
	    result.put("updateCount", updateCount);
	    result.put("failedRows", failedRows);

	    return result;
	}


    @Transactional
    public int delete_common_code(List<Common_codeDTO> deletedRows, String code) {
        int deletedCount = 0;

        if (code == null || code.isEmpty()) {
            for (Common_codeDTO row : deletedRows) {
                String groupCode = row.getCommon_cc();

                deletedCount += commonRepository.deleteGroupCode(groupCode);
                deletedCount += commonRepository.deleteCommonCodeList(groupCode);
            }
        } else {
            for (Common_codeDTO row : deletedRows) {
                String commonCode = row.getCommon_cc();

                deletedCount += commonRepository.deleteCommonCode(code, commonCode);
            }
        }

        return deletedCount;
    }


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
	

}
