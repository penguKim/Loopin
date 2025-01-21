package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.ExcelDTO;
import com.itwillbs.c4d2412t3p1.util.ExcelDownloader;

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
