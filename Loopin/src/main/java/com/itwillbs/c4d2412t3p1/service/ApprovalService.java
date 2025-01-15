package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.entity.Common_code;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.repository.ApprovalRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class ApprovalService {
	
	private final ApprovalRepository approvalRepository;
	
	private final CommonRepository commonRepository;
	
	// 결재 현황 조회
	public List<Approval> findAll() {
		
		return approvalRepository.findAll();
	}


	// 직원 등록
	@Transactional
    public void insert_APPROVAL(ApprovalDTO approvalDTO) throws IOException {
        
        // 시퀀스 값 가져오기
        Long sequenceValue = approvalRepository.getNextSequenceValue();
        System.out.println("Next Sequence Value: " + sequenceValue);

        Approval approval = Approval.createApproval(approvalDTO, sequenceValue);
        
        approvalRepository.save(approval);
        System.out.println("@@@@@@@@@" + approval);
    }

	
//	 직원 업데이트
	public void update_APPROVAL(ApprovalDTO approvalDTO) {
	    // 직원 조회
		Approval approval = approvalRepository.findById(approvalDTO.getApproval_cd())
	            .orElseThrow(() -> new IllegalArgumentException("해당 직원이 존재하지 않습니다."));

	    // 엔티티 업데이트
		approval.setEmployeeEntity(approval, approvalDTO);

	    // 데이터베이스 저장
		approvalRepository.save(approval);
	}
	

    // ID로 Approval 조회
    public Approval findEmployeeById(String approval_cd) {
        // Repository를 사용하여 데이터 조회
        return approvalRepository.findById(approval_cd)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 데이터를 찾을 수 없습니다."));
    }
	
    
    // 직원 삭제
	public void delete_APPROVAL(List<String> cds) {
		approvalRepository.deleteAllById(cds);
		
	}

	// 기안서 구분 데이터 조회
	public List<Common_code> selectDRAFTList(String string) {
		return commonRepository.selectDRAFTList("00", string);
	}
	
	
	// 직원 코드로 데이터 조회
	public List<Approval> findByApprovalCd(String currentCd) {
		return approvalRepository.findByApprovalCd(currentCd);
	}
	
}
