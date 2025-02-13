package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.ContractDetailDTO;
import com.itwillbs.c4d2412t3p1.domain.ProductPlanDTO;
import com.itwillbs.c4d2412t3p1.entity.ContractDetail;
import com.itwillbs.c4d2412t3p1.entity.Productplan;
import com.itwillbs.c4d2412t3p1.repository.ApprovalRepository;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.ContractRepository;
import com.itwillbs.c4d2412t3p1.repository.ProductplanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class ProductplanService {

	private final ProductplanRepository productplanRepository;
	
	private final ContractRepository contractRepository;
	
	public List<ContractDetailDTO> select_CONTRACTCD_list(String contractCd) {
        List<ContractDetail> contractDetails = contractRepository.findByContractCd(contractCd);
        
        return contractDetails.stream()
                .map(detail -> new ContractDetailDTO(
                        detail.getContract_cd(),
                        detail.getProduct_cd(),
                        detail.getProduct_sz(),
                        detail.getProduct_cr(),
                        detail.getProduct_am(),
                        detail.getContract_ct(),
                        detail.getContract_ed(),
                        detail.getProduct_un()
                ))
                .collect(Collectors.toList());
    }

}
