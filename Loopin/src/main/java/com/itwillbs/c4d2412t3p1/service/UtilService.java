package com.itwillbs.c4d2412t3p1.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.config.EmployeeDetails;
import com.itwillbs.c4d2412t3p1.domain.ExcelDTO;
import com.itwillbs.c4d2412t3p1.util.ExcelDownloader;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class UtilService {
	
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
