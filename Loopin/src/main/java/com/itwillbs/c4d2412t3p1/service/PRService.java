package com.itwillbs.c4d2412t3p1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.PRCalDTO;
import com.itwillbs.c4d2412t3p1.domain.PRDTO;
import com.itwillbs.c4d2412t3p1.entity.PRCode;
import com.itwillbs.c4d2412t3p1.mapper.PRMapper;
import com.itwillbs.c4d2412t3p1.repository.PRCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PRService {

	private final PRMapper prM;
	private final PRCodeRepository prcRep;
//	private final EmployeeRepository empRep;
	
	public List<PRCode> getprcode() {

		List<PRCode> list = prcRep.findAll();
		
		return list;
	}

	public List<Map<String, Object>> selectpradmin() {

		List<Map<String, Object>> list = prM.selectpradmin();
		
		return list;
	}

	public List<Map<String, Object>> selectpradminfirstmodal(Long pr_id) {

		List<Map<String, Object>> list = prM.selectpradminfirstmodal(pr_id);
		
		return list;
	}

	public List<Map<String, Object>> selectpradminfirstmodal2(Long prdetail_id) {
		
		List<Map<String, Object>> list = prM.selectpradminfirstmodal2(prdetail_id);
		
		return list;
	}

	public List<PRDTO> calculatingMachine(String bs, String pt, String nt, String wt, String ht, String aa) {

		List<PRCode> formulas = prcRep.findAll();
		List<PRDTO> calculatedSalary = new ArrayList<>();
		List<PRCalDTO> calculated = new ArrayList<>();
		
		double BS = Double.parseDouble(bs);
		double workingtime = 209;
		double nightworkingtime = Double.parseDouble(nt);
		double weekendworkingtime = Double.parseDouble(wt);
		double holydayworkingtime = Double.parseDouble(ht);
		double leastannual = Double.parseDouble(aa);
				
		for (PRCode formula : formulas) {
		}
		
		
		return null;
	}

	
}
