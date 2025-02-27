package com.itwillbs.c4d2412t3p1.controller;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class MenuController {

    @GetMapping("/header")
    public Map<String, Object> getHeaderMenu(Authentication authentication) {
        Map<String, Object> menuData = new HashMap<>();
        List<Map<String, String>> headerMenu = new ArrayList<>();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isSysAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_SYS_ADMIN"));
        boolean isHrAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_HR_ADMIN"));
        boolean isAtAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_AT_ADMIN"));
        boolean isPrAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PR_ADMIN"));
        boolean isBnAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_BN_ADMIN"));
        boolean isMfAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MF_ADMIN"));


        // ✅ 헤더 메뉴 설정
        if (isSysAdmin) {
        	headerMenu.add(Map.of("name", "시스템권한", "dataMenu", "SYS"));
            headerMenu.add(Map.of("name", "ERP", "dataMenu", "ERP"));
            headerMenu.add(Map.of("name", "MES", "dataMenu", "MES"));
        } else {
            if (isHrAdmin || isAtAdmin || isPrAdmin || isBnAdmin) {
                headerMenu.add(Map.of("name", "ERP", "dataMenu", "ERP"));
            }
            if (isMfAdmin) {
                headerMenu.add(Map.of("name", "MES", "dataMenu", "MES"));
            }
        }

        // ✅ 모든 직원이 볼 수 있는 메뉴
        headerMenu.add(Map.of("name", "공지사항", "dataMenu", "NOTICE", "link", "/notice_list"));
        headerMenu.add(Map.of("name", "전자결재", "dataMenu", "APPROVAL", "link", "/approval_list"));
        headerMenu.add(Map.of("name", "나의 메뉴", "dataMenu", "MYMENU"));

        menuData.put("headerMenus", headerMenu);
        return menuData;
    }

    @GetMapping("/sidebar")
    public Map<String, Object> getSidebarMenu(Authentication authentication,
    		@RequestParam(value = "menuType", required = false, defaultValue = "MYMENU") String menuType) {
        Map<String, Object> menuData = new HashMap<>();
        List<Map<String, String>> sidebarMenu = new ArrayList<>();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        boolean isSysAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_SYS_ADMIN"));
        boolean isHrAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_HR_ADMIN"));
        boolean isAtAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_AT_ADMIN"));
        boolean isPrAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PR_ADMIN"));
        boolean isBnAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_BN_ADMIN"));
        boolean isMfAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_MF_ADMIN"));

        // ✅ 사이드바 메뉴 설정 (menuType에 따라 필터링)
        if ("ERP".equals(menuType)) {
        	if(isHrAdmin || isSysAdmin) {
        		sidebarMenu.add(Map.of("name", "인사", "link", "1"));
        		sidebarMenu.add(Map.of("name", "인사카드", "link", "/employee_list"));
        		sidebarMenu.add(Map.of("name", "인사현황", "link", "/employee_chart" ));
        		sidebarMenu.add(Map.of("name", "인사발령", "link", "/transfer_list" ));
        	}
        	if(isAtAdmin || isSysAdmin) {
        		sidebarMenu.add(Map.of("name", "근태", "link", "1"));
        		sidebarMenu.add(Map.of("name", "연차등록", "link", "/annual_regist" ));
        		sidebarMenu.add(Map.of("name", "휴일등록", "link", "/holiday_regist" ));
        		sidebarMenu.add(Map.of("name", "휴가조회", "link", "/attendance_list" ));
        		sidebarMenu.add(Map.of("name", "근로관리", "link", "/commute_type" ));
        		sidebarMenu.add(Map.of("name", "출퇴근기록부", "link", "/commute" ));
        		sidebarMenu.add(Map.of("name", "출퇴근현황", "link", "/commute_chart" ));
        	}
        	if(isPrAdmin || isSysAdmin) {
        		sidebarMenu.add(Map.of("name", "급여", "link", "1"));
        		sidebarMenu.add(Map.of("name", "급여이력조회", "link", "/check_pradmin" ));
        		sidebarMenu.add(Map.of("name", "급여이체", "link", "/prsend" ));
        		sidebarMenu.add(Map.of("name", "급여코드", "link", "/prcode" ));
        	}
        	if(isBnAdmin || isSysAdmin) {
        		sidebarMenu.add(Map.of("name", "영업", "link", "1"));
        		sidebarMenu.add(Map.of("name", "수주관리", "link", "/contract_list" ));
        		sidebarMenu.add(Map.of("name", "발주관리", "link", "/order_list" ));
        		sidebarMenu.add(Map.of("name", "출하관리", "link", "/shipment_list" ));
        		sidebarMenu.add(Map.of("name", "영업현황", "link", "/business_state" ));
        	}
        }
        if ("MES".equals(menuType)) {
        	if(isSysAdmin) {
        		sidebarMenu.add(Map.of("name", "기준정보", "link", "1"));
        		sidebarMenu.add(Map.of("name", "품목관리", "link", "/product_list" ));
        		sidebarMenu.add(Map.of("name", "거래처관리", "link", "/account_list" ));
        		sidebarMenu.add(Map.of("name", "창고관리", "link", "/warehouse_list" ));
        		sidebarMenu.add(Map.of("name", "설비관리", "link", "/equipment_list" ));
        		sidebarMenu.add(Map.of("name", "공정관리", "link", "/process" ));
        	}
        	if(isMfAdmin || isSysAdmin) {
        		sidebarMenu.add(Map.of("name", "자재", "link", "1"));
        		sidebarMenu.add(Map.of("name", "재고관리", "link", "/stock_list" ));
        		sidebarMenu.add(Map.of("name", "입출고관리", "link", "/inout_list" ));
        	}
        	if(isMfAdmin || isSysAdmin) {
        		sidebarMenu.add(Map.of("name", "생산", "link", "1"));
        		sidebarMenu.add(Map.of("name", "BOM", "link", "/bom" ));
        		sidebarMenu.add(Map.of("name", "생산계획", "link", "/product_plan_list" ));
        		sidebarMenu.add(Map.of("name", "작업지시", "link", "/workorder_list" ));
        		sidebarMenu.add(Map.of("name", "로트추적", "link", "/lot_list" ));
        	}
        }
        if ("SYS".equals(menuType)) {
        	sidebarMenu.add(Map.of("name", "공통코드", "link", "/common_code" ));
        	sidebarMenu.add(Map.of("name", "로그", "link", "/log_list" ));
        }
        if ("MYMENU".equals(menuType)) {
        	sidebarMenu.add(Map.of("name", "나의 인사", "link", "1"));
            sidebarMenu.add(Map.of("name", "인사카드", "link", "/employee_list?type=1" ));
            sidebarMenu.add(Map.of("name", "인사발령", "link", "/transfer_list?type=1" ));
            sidebarMenu.add(Map.of("name", "나의 근태", "link", "1"));
            sidebarMenu.add(Map.of("name", "휴가조회", "link", "/attendance_list?type=1" ));
            sidebarMenu.add(Map.of("name", "출퇴근현황", "link", "/commute?type=1" ));
            sidebarMenu.add(Map.of("name", "나의 급여", "link", "1"));
            sidebarMenu.add(Map.of("name", "급여조회", "link", "/check_salary" ));
            sidebarMenu.add(Map.of("name", "급여계산기", "link", "/prcal" ));
        }
        if ("NOTICE".equals(menuType)) {
        	sidebarMenu.add(Map.of("name", "공지사항", "link", "/notice_list" ));
        }
        if("APPROVAL".equals(menuType)) {
        	sidebarMenu.add(Map.of("name", "전자결재", "link", "/approval_list" ));
        }

        menuData.put("sidebarMenus", sidebarMenu);
        return menuData;
    }

}