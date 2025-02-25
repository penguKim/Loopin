package com.itwillbs.c4d2412t3p1.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.ExcelDTO;
import com.itwillbs.c4d2412t3p1.util.ExcelDownloader;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@Log4j2
public class UtilService {
	
    @Value("${file.upload-dir}")
    private String uploadDir;
	
    
	private final ExcelDownloader excelDownloader;

	
    // null 체크
	public String checkNull(String str) {
	    return str == null ? "" : str;
	}
	
	public String checkNull(String str, String defaultValue) {
		return str == null ? defaultValue : str;
	}

	public int checkNull(Integer num) {
	    return num == null ? 0 : num;
	}

	public boolean checkNull(Boolean bool) {
	    return bool == null ? false : bool;
	}

    public Long checkNull(Long num) {
    	return num == null ? 0L : num;
    }
    
    public Double checkNull(Double num) {
    	return num == null ? 0.0 : num;
    }
    
	// UUID 생성
	public String setUUID(String fileName) {
		return UUID.randomUUID().toString() + "_" + fileName;
	}
    
	// 파일 저장
    public <T> void setFile(String tableName, MultipartFile file, Consumer<String> setter) throws IOException {
        if (file != null && !file.isEmpty()) {
            String uniqueFileName = setUUID(file.getOriginalFilename());
            String dateDir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            Path uploadPath = Paths.get(uploadDir, tableName, dateDir).resolve(uniqueFileName);
            
            if (!Files.exists(uploadPath.getParent())) {
                Files.createDirectories(uploadPath.getParent());
            }
            log.info(">>>>>>>> 파일 경로 : " + Files.exists(uploadPath.getParent()));
            
            
            
            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            setter.accept(Paths.get(tableName, dateDir, uniqueFileName).toString().replace(File.separator, "/"));
        }
    }
    
    // 파일 삭제
    public void deleteFile(String filePath) throws IOException {
        try {
			if (filePath != null && !filePath.isEmpty()) {
			    Path fullPath = Paths.get(uploadDir, filePath);
			    Files.deleteIfExists(fullPath);
			    System.out.println("파일 삭제했어 : " + fullPath);
			    // 빈 디렉토리 삭제 (선택적)
			    Path parent = fullPath.getParent();
			    if (Files.exists(parent) && Files.isDirectory(parent) && isDirEmpty(parent)) {
			        Files.delete(parent);
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    // 빈 디렉토리 확인
    private boolean isDirEmpty(Path path) throws IOException {
        try (DirectoryStream<Path> directory = Files.newDirectoryStream(path)) {
            return !directory.iterator().hasNext();
        }
    }
    
    
	// 시큐리티 사원 정보
	public EmployeeDetails getEmployee() {
		return (EmployeeDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	// 사원 권한 체크
	public boolean isAuthority(String... authorities) {
	    Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	            
	    for (String authority : authorities) {
	        String roleAuthority = "ROLE_" + authority;
	        for (GrantedAuthority userAuth : userAuthorities) {
	            if (userAuth.getAuthority().equals(roleAuthority)) {
	                return true;
	            }
	        }
	    }
	    return false;
	}

	// 사원 권한 체크
	public boolean isNotAuthority(String... authorities) {
	    Collection<? extends GrantedAuthority> userAuthorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
	            
	    for (String authority : authorities) {
	        String roleAuthority = "ROLE_" + authority;
	        for (GrantedAuthority userAuth : userAuthorities) {
	            if (userAuth.getAuthority().equals(roleAuthority)) {
	                return false;
	            }
	        }
	    }
	    return true;
	}
	
	// 엑셀 다운로드
    public void excelDownloader(ExcelDTO excel, OutputStream outputStream) throws IOException {
        excelDownloader.downloadExcel(excel, outputStream);
    }
    
    
}
