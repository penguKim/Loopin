package com.itwillbs.c4d2412t3p1.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.itwillbs.c4d2412t3p1.domain.ExcelDTO;
import com.itwillbs.c4d2412t3p1.service.UtilService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Controller
@Log
public class UtilController {
	
	private final UtilService utilService;

	@PostMapping("/excelDownload")
	public void excelDownloader(@RequestBody ExcelDTO excel, HttpServletResponse response) throws IOException {
	    try {
	        String fileName = URLEncoder.encode(excel.getTitle(), "UTF-8");
	        
	        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	        response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xlsx");
	        
	        utilService.excelDownloader(excel, response.getOutputStream());
	    } catch (Exception e) {
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        e.printStackTrace();
	    }
	}


}
