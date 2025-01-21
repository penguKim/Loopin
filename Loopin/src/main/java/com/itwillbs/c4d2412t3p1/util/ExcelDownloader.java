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
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Component;

import com.itwillbs.c4d2412t3p1.domain.ExcelDTO;

@Component
public class ExcelDownloader {
    
    public void downloadExcel(ExcelDTO excel, OutputStream outputStream) throws IOException {
        try (SXSSFWorkbook workbook = new SXSSFWorkbook(100)) { // 100개 행마다 플러시
            workbook.setCompressTempFiles(true); // 임시 파일 압축
            
            Sheet sheet = workbook.createSheet(excel.getTitle());
            
            // 헤더 스타일 설정
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            setBorder(headerStyle);
            
            CellStyle rowStyle = workbook.createCellStyle();
            setBorder(rowStyle);
            
            // 헤더 생성
            createHeaders(sheet, headerStyle, excel.getHeaders());
            
            // 데이터 행 생성
            createDataRows(sheet, rowStyle, excel.getHeaders(), excel.getRows());
            
            // 컬럼 너비
            setColumnWidth(sheet, excel.getHeaders(), excel.getRows());
            
            workbook.write(outputStream);
            
            // 임시 파일 정리
            workbook.dispose();
        }
    }
    
    private void setBorder(CellStyle style) {
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
    
    private void createHeaders(Sheet sheet, CellStyle style, List<Map<String, Object>> headers) {
        Row headerRow = sheet.createRow(0);
        headerRow.setHeight((short)(headerRow.getHeight() * 1.5));
        
        // No. 컬럼 추가
        Cell noCell = headerRow.createCell(0);
        noCell.setCellValue("No.");
        noCell.setCellStyle(style);
        
        int columnIndex = 1;  // 1부터 시작
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
        int maxColumn = getVisibleColumnCount(headers) + 1; // No. 컬럼 포함
        
        for (Map<String, Object> rowData : rows) {
            Row row = sheet.createRow(rowIndex);
            
            // 번호 추가
            Cell noCell = row.createCell(0);
            noCell.setCellValue(rowIndex);
            noCell.setCellStyle(style);
            
            int columnIndex = 1;  // 1부터 시작
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
            rowIndex++;
        }
        
        // 총계 행 추가 (마지막 컬럼에)
        Row totalRow = sheet.createRow(rowIndex);
        totalRow.setHeight((short)(totalRow.getHeight() * 1.3)); // 높이 증가
        for (int i = 0; i < maxColumn - 1; i++) {
            Cell emptyCell = totalRow.createCell(i);
            emptyCell.setCellStyle(style);
        }
        Cell totalCell = totalRow.createCell(maxColumn - 1);
        totalCell.setCellValue("총 " + rows.size() + "건");
        totalCell.setCellStyle(style);
    }

    private void setColumnWidth(Sheet sheet, List<Map<String, Object>> headers, List<Map<String, Object>> rows) {
        // No. 컬럼 너비 설정
        sheet.setColumnWidth(0, 8 * 256);
        
        int columnIndex = 1;  // 1부터 시작
        for (Map<String, Object> header : headers) {
            if (!(boolean)header.get("hidden")) {
                String headerText = (String)header.get("header");
                int maxWidth = getKoreanWidth(headerText);
                
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
                
                sheet.setColumnWidth(columnIndex, (maxWidth + 2) * 256);
                columnIndex++;
            }
        }
    }

    private int getVisibleColumnCount(List<Map<String, Object>> headers) {
        return (int) headers.stream()
                .filter(header -> !(boolean)header.get("hidden"))
                .count();
    }
    
    private int getKoreanWidth(String text) {
        int width = 0;
        for (char c : text.toCharArray()) {
            width += (c > 127 || c < 32) ? 2 : 1;
        }
        return width;
    }
}