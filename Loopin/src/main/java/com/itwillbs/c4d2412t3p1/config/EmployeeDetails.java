package com.itwillbs.c4d2412t3p1.config;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.itwillbs.c4d2412t3p1.entity.Employee;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDetails extends User {
    
    private final String employee_cd;
    private final String employee_dp;
    private final String employee_gd;
    private final String employee_hd;
    private final String employee_rd;
    private final String employee_rr;
    private final String employee_cg;
    private final String employee_nt;
    private final String employee_nm;
    private final String employee_bd;
    private final String employee_ad;
    private final String employee_sb;
    private final String employee_ph;
    private final String employee_em;
    private final String employee_pi;
    private final BigDecimal employee_bs;
    private final String employee_bk;
    private final String employee_an;
    private final String employee_dt;
    private final Boolean employee_mg;
    private final String employee_rl;
	private final Boolean employee_us;
	private final String workinghour_id;
    
    public EmployeeDetails(Employee employee) {
        super(
            employee.getEmployee_id(),
            employee.getEmployee_pw(),
            List.of(new SimpleGrantedAuthority("ROLE_" + employee.getEmployee_rl()))
        );
        this.employee_cd = employee.getEmployee_cd();
        this.employee_dp = employee.getEmployee_dp();
        this.employee_gd = employee.getEmployee_gd();
        this.employee_hd = employee.getEmployee_hd();
        this.employee_rd = employee.getEmployee_rd();
        this.employee_rr = employee.getEmployee_rr();
        this.employee_cg = employee.getEmployee_cg();
        this.employee_nt = employee.getEmployee_nt();
        this.employee_nm = employee.getEmployee_nm();
        this.employee_bd = employee.getEmployee_bd();
        this.employee_ad = employee.getEmployee_ad();
        this.employee_sb = employee.getEmployee_sb();
        this.employee_ph = employee.getEmployee_ph();
        this.employee_em = employee.getEmployee_em();
        this.employee_pi = employee.getEmployee_pi();
        this.employee_bs = employee.getEmployee_bs();
        this.employee_bk = employee.getEmployee_bk();
        this.employee_an = employee.getEmployee_an();
        this.employee_dt = employee.getEmployee_dt();
        this.employee_mg = employee.getEmployee_mg();
        this.employee_rl = employee.getEmployee_rl();
        this.employee_us = employee.getEmployee_us();
        this.workinghour_id = employee.getWorkinghour_id();
    }
}

