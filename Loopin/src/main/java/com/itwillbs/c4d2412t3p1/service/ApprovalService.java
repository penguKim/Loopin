package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.domain.ApprovalDTO;
import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;
import com.itwillbs.c4d2412t3p1.entity.Approval;
import com.itwillbs.c4d2412t3p1.entity.Employee;
import com.itwillbs.c4d2412t3p1.repository.ApprovalRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class ApprovalService {
	
	private final ApprovalRepository approvalRepository;
	
	
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

}
