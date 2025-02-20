package com.itwillbs.c4d2412t3p1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.BomallDTO;
import com.itwillbs.c4d2412t3p1.entity.Bom;
import com.itwillbs.c4d2412t3p1.entity.BomProcess;
import com.itwillbs.c4d2412t3p1.entity.Product;
import com.itwillbs.c4d2412t3p1.mapper.BomMapper;
import com.itwillbs.c4d2412t3p1.repository.BomProcessRepository;
import com.itwillbs.c4d2412t3p1.repository.BomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BomService {
	
	private final BomRepository bR;
	private final BomProcessRepository bpR;
	private final BomMapper bM;
	
	public List<Product> selectPD() {

		List<Product> list = bM.selectPD();
		
		return list;
	}

	public List<BomProcess> selectBomAll() {
		return bM.selectBomAll();
	}

	public Integer checkpcd(String pcd) {
		
		String result = bM.checkpcd(pcd);
		
		if(result == null) {
			return 0;
		}else {
			return 1;
		}
	}

	public List<Process> selectPCs() {
		
		List<Process> list = bM.selectPCs();
		
		return list;
	}

	public List<Product> selectbom(List<String> ckrowpds) {

		List<Product> list = bM.selectbom(ckrowpds);
		
		return list;
	}

	public List<BomallDTO> insertbom(List<BomallDTO> bom, List<BomallDTO> bompc) {
		// TODO Auto-generated method stub
		return null;
	}


}
