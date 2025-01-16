package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.NoticeDTO;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.entity.Notice;
import com.itwillbs.c4d2412t3p1.repository.NoticeRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.NoticeFilterRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class NoticeService {
	
	private final NoticeRepository noticeRepository;
	
	// 공지사항 현황 조회
	public List<Notice> findAll() {
		
		return noticeRepository.findAll();
	}


	// 공지사항 등록
	@Transactional
    public void insert_NOTICE(NoticeDTO noticeDTO) throws IOException {
        
        // 시퀀스 값 가져오기
        Long sequenceValue = noticeRepository.getNextSequenceValue();
        System.out.println("Next Sequence Value: " + sequenceValue);

        Notice notice = Notice.createNotice(noticeDTO, sequenceValue);
        
        noticeRepository.save(notice);
        System.out.println("@@@@@@@@@" + notice);
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
	
	
    public List<Notice> select_FILTERED_NOTICE(NoticeFilterRequest filterRequest) {
    	return noticeRepository.select_FILTERED_NOTICE(filterRequest);
    }
	
	
}
