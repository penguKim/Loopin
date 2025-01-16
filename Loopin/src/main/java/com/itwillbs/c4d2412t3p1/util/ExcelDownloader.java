package com.itwillbs.c4d2412t3p1.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import com.itwillbs.c4d2412t3p1.domain.ExcelDTO;


@Component
public class ExcelDownloader {
    
    public void downloadExcel(ExcelDTO excel, OutputStream outputStream) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(excel.getTitle());
            
            // 헤더 스타일 설정
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setBorder(headerStyle);
            
            CellStyle rowStyle = workbook.createCellStyle();
            setBorder(rowStyle);
            
            // 헤더 생성
            createHeaders(sheet, headerStyle, excel.getHeaders());
            
            // 데이터 행 생성
            createDataRows(sheet, rowStyle, excel.getHeaders(), excel.getRows());
            
            // 컬럼 너비 최적화
            optimizeColumnWidth(sheet, excel.getHeaders(), excel.getRows());
            
            workbook.write(outputStream);
        }
    }
    
    private void setBorder(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
    
    private void createHeaders(Sheet sheet, CellStyle style, List<Map<String, Object>> headers) {
        Row headerRow = sheet.createRow(0);
        headerRow.setHeight((short)(headerRow.getHeight() * 1.5)); // 헤더 행 높이 설정
        
        int columnIndex = 0;
        for (Map<String, Object> header : headers) {
            if (!(boolean)header.get("hidden")) {
                Cell cell = headerRow.createCell(columnIndex++);
                cell.setCellValue((String)header.get("header"));
                cell.setCellStyle(style);
            }
        }
    }
    
    private void createDataRows(Sheet sheet, CellStyle style, List<Map<String, Object>> headers, List<Map<String, Object>> rows) {
        int rowIndex = 1;
        
        for (Map<String, Object> rowData : rows) {
            Row row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            
            for (Map<String, Object> header : headers) {
                if (!(boolean)header.get("hidden")) {
                    Cell cell = row.createCell(columnIndex++);
                    String fieldName = (String)header.get("name");
                    Object value = rowData.get(fieldName);
                    
                    if (value != null) {
                        cell.setCellValue(value.toString());
                    }
                    cell.setCellStyle(style);
                }
            }
        }
    }

    
    private void optimizeColumnWidth(Sheet sheet, List<Map<String, Object>> headers, List<Map<String, Object>> rows) {
        int columnIndex = 0;
        for (Map<String, Object> header : headers) {
            if (!(boolean)header.get("hidden")) {
                // 헤더 텍스트의 길이로 초기 너비 설정
                String headerText = (String)header.get("header");
                int maxWidth = getKoreanWidth(headerText);
                
                // 해당 컬럼의 모든 데이터를 검사하여 최대 길이 찾기
                String fieldName = (String)header.get("name");
                for (Map<String, Object> row : rows) {
                    Object value = row.get(fieldName);
                    if (value != null) {
                        int valueWidth = getKoreanWidth(value.toString());
                        if (valueWidth > maxWidth) {
                            maxWidth = valueWidth;
                        }
                    }
                }
                
                // 여백을 포함하여 컬럼 너비 설정 (256은 1문자 너비)
                sheet.setColumnWidth(columnIndex, (maxWidth + 2) * 256);
                columnIndex++;
            }
        }
    }

    
    private int getKoreanWidth(String text) {
        int width = 0;
        for (char c : text.toCharArray()) {
            width += (c > 127 || c < 32) ? 2 : 1;
        }
        return width;
    }
}



