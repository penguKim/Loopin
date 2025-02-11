package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.domain.StockDTO;
import com.itwillbs.c4d2412t3p1.mapper.StockMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class StockService {
	
	private final UtilService util;
	private final StockMapper stockMapper;

	// 재고 조회
	public List<Map<String, Object>> select_STOCK_list(Map<String, List<Common_codeDTO>> sizeList) {
		return stockMapper.select_STOCK_list(sizeList);
	}



}
