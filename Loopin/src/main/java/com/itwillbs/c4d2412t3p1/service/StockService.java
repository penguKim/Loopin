package com.itwillbs.c4d2412t3p1.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itwillbs.c4d2412t3p1.domain.Common_codeDTO;
import com.itwillbs.c4d2412t3p1.mapper.StockMapper;
import com.itwillbs.c4d2412t3p1.repository.StockRepository;
import com.itwillbs.c4d2412t3p1.util.FilterRequest.StockFilterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class StockService {
	
	private final UtilService util;
	private final StockMapper stockMapper;
	private final StockRepository stockRepository;

	// 재고 조회
	public List<Map<String, Object>> select_STOCK_list(StockFilterRequest filter, Map<String, List<Common_codeDTO>> sizeList) {
		return stockMapper.select_STOCK_list(filter, sizeList);
	}

	// 재고 차트 조회
	public List<Map<String, Object>> select_STOCK_chart(StockFilterRequest filter) {
		return stockMapper.select_STOCK_chart(filter);
	}

	// 재고 삭제
	@Transactional
	public void delete_STOCK(List<Map<String, Object>> stockList) {
		List<String> combinedList = new ArrayList<>();
		String warehouseCd = null;
		String wareareaCd = null;

		for (Map<String, Object> stock : stockList) {
		    String itemCd = (String) stock.get("ITEM_CD");
		    String color = (String) stock.get("COLOR");

		    if (warehouseCd == null) {
		        warehouseCd = (String) stock.get("WAREHOUSE_CD");
		    }
		    if (wareareaCd == null) {
		        wareareaCd = (String) stock.get("WAREAREA_CD");
		    }

		    for (String key : stock.keySet()) {
		        if (key.startsWith("SIZE_")) {
		            Object valueObj = stock.get(key);
		            int sizeCount = Integer.parseInt(valueObj.toString());
		            if (sizeCount > 0) {
		                String sizeValue = key.substring(key.indexOf("_") + 1);
		                String combined = itemCd + "-" + sizeValue + "-" + color;
		                combinedList.add(combined);
		            }
		        }
		    }
		    
		    if (warehouseCd != null && wareareaCd != null && !combinedList.isEmpty()) {
		    	stockRepository.deleteByCombinedIds(combinedList, warehouseCd, wareareaCd);
		    	combinedList.clear();
		    	warehouseCd = null;
		    	wareareaCd = null;
		    }
		}

	}

}
