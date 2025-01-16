package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.ExcelDTO;
import com.itwillbs.c4d2412t3p1.repository.CommonRepository;
import com.itwillbs.c4d2412t3p1.repository.EmployeeRepository;
import com.itwillbs.c4d2412t3p1.repository.TransferRepository;
import com.itwillbs.c4d2412t3p1.util.ExcelDownloader;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class UtilService {
	
	private final ExcelDownloader excelDownloader;

    public void excelDownloader(ExcelDTO excel, OutputStream outputStream) throws IOException {
        excelDownloader.downloadExcel(excel, outputStream);
    }

}
