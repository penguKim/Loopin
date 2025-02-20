package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.InoutDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;
import com.itwillbs.c4d2412t3p1.mapper.InoutMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class InoutService {
	
	private final UtilService util;
	private final InoutMapper inoutMapper;

	// 입출고 조회
	public List<InoutDTO> select_INOUT_list() {
		return inoutMapper.select_INOUT_list();
	}

	// 발주 조회
	public List<InoutDTO> select_INOUT_ORDER() {
		return inoutMapper.select_INOUT_ORDER();
	}

	// 발주품목 조회
	public List<InoutDTO> select_INOUT_ORDERDETAIL() {
		// TODO Auto-generated method stub
		return inoutMapper.select_INOUT_ORDERDETAIL();
	}


}
