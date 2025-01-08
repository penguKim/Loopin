package com.itwillbs.c4d2412t3p1.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.entity.PRCode;
import com.itwillbs.c4d2412t3p1.repository.PRCodeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PRService {

	private final PRCodeRepository prcRep;
	
	public List<PRCode> getprcode() {

		List<PRCode> list = prcRep.findAll();
		
		return list;
	}

}
