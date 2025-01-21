package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.NoticeDTO;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Notice;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.NoticeRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.NoticeFilterRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class NoticeService {
	
	private final CommonRepository commonRepository;
	
	private final NoticeRepository noticeRepository;
	
//	// 공지사항 현황 조회
//	public List<Notice> findAll() {
//		
//		return noticeRepository.findAll();
//	}
	
	
	
	public List<Map<String, Object>> select_NOTICE_LIST() {
	    List<Object[]> result = noticeRepository.select_NOTICE_LIST();

	    return result.stream().map(row -> {
	        Map<String, Object> notice = new HashMap<>();
	        notice.put("notice_cd", row[0]);
	        notice.put("notice_tt", row[1]);
	        notice.put("notice_ct", row[2]);
	        notice.put("notice_wr", row[3]);
	        
	        // notice_wd (작성일) 포맷팅 처리
	        if (row[4] != null && row[4] instanceof Timestamp) {
	            Timestamp noticeWd = (Timestamp) row[4];  // Timestamp 객체로 강제 변환
	            // Timestamp -> LocalDateTime -> String (포맷팅)
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	            String formattedDate = noticeWd.toLocalDateTime().format(formatter);  // 포맷팅
	            notice.put("notice_wd", formattedDate);  // 포맷팅된 날짜를 notice_wd에 넣음
	        } else {
	        	notice.put("notice_wd", "");  // notice_wd가 null이거나 다른 타입일 경우 빈 문자열 처리
	        }
	        
	        notice.put("notice_mf", row[5]);
	        notice.put("notice_md", row[6]);
	        notice.put("employee_nm", row[7]);
	        notice.put("employee_dp", row[8]);

	        System.out.println("SSS@@@@@@@@" + notice);
	        return notice;
	    }).collect(Collectors.toList());
	}


	// 공지사항 등록
	@Transactional
    public void insert_NOTICE(NoticeDTO noticeDTO) throws IOException {
        
        // 시퀀스 값 가져오기
        Long sequenceValue = noticeRepository.getNextSequenceValue();
        System.out.println("Next Sequence Value: " + sequenceValue);

        Notice notice = Notice.createNotice(noticeDTO, sequenceValue);
        
        noticeRepository.save(notice);
//        System.out.println("@@@@@@@@@" + notice);
    }

	
//	 공지사항 업데이트
	public void update_NOTICE(NoticeDTO noticeDTO) {
	    // 직원 조회
		 Notice notice = noticeRepository.findById(noticeDTO.getNotice_cd())
	            .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 존재하지 않습니다."));

		 // 엔티티 업데이트
		 notice.setNoticeEntity(notice, noticeDTO);

		 // 데이터베이스 저장
		 noticeRepository.save(notice);
	}
	

    // ID로 Notice 조회
    public Notice findNoticeById(String notice_cd) {
        // Repository를 사용하여 데이터 조회
        return noticeRepository.findById(notice_cd)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 데이터를 찾을 수 없습니다."));
    }
	
    
    // 직원 삭제
	public void delete_NOTICE(List<String> cds) {
		noticeRepository.deleteAllById(cds);
	}
	
	
    // 공통코드 데이터 조회
    public List<Common_code> selectCommonList(String string) {
    	return commonRepository.selectCommonList("00", string);
    }
	
	
	
	
    public List<Map<String, Object>> select_FILTERED_NOTICE(NoticeFilterRequest filterRequest) {
    	
	    List<Object[]> result = noticeRepository.select_FILTERED_NOTICE(filterRequest);

	    return result.stream().map(row -> {
	        Map<String, Object> notice = new HashMap<>();
	        notice.put("notice_cd", row[0]);
	        notice.put("notice_tt", row[1]);
	        notice.put("notice_ct", row[2]);
	        notice.put("notice_wr", row[3]);
	        
	        // notice_wd (작성일) 포맷팅 처리
	        if (row[4] != null && row[4] instanceof Timestamp) {
	            Timestamp noticeWd = (Timestamp) row[4];  // Timestamp 객체로 강제 변환
	            // Timestamp -> LocalDateTime -> String (포맷팅)
	            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
	            String formattedDate = noticeWd.toLocalDateTime().format(formatter);  // 포맷팅
	            notice.put("notice_wd", formattedDate);  // 포맷팅된 날짜를 notice_wd에 넣음
	        } else {
	        	notice.put("notice_wd", "");  // notice_wd가 null이거나 다른 타입일 경우 빈 문자열 처리
	        }
	        
	        notice.put("notice_mf", row[5]);
	        notice.put("notice_md", row[6]);
	        notice.put("employee_nm", row[7]);
	        notice.put("employee_dp", row[8]);

	        System.out.println("SSS@@@@@@@@" + notice);
	        return notice;
	    }).collect(Collectors.toList());
    }
	
    
    
    
    
	
}
