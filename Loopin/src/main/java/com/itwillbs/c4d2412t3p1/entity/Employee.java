package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;


import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@ToString
public class Employee {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "employee_cd")
	private Long employee_cd;

    @Column(name = "employee_id")
    private String employee_id;
    
    @Column(name = "employee_pw")
    private String employee_pw;

    @Column(name = "employee_dp")
    private String employee_dp;

    @Column(name = "employee_gd")
    private String employee_gd;

    @Column(name = "employee_hd")
    private String employee_hd;

    @Column(name = "employee_rd")
    private String employee_rd;

    @Column(name = "employee_rr")
    private String employee_rr;

    @Column(name = "employee_cg")
    private String employee_cg;

    @Column(name = "employee_nt")
    private String employee_nt;

    @Column(name = "employee_nm")
    private String employee_nm;

    @Column(name = "employee_bd")
    private String employee_bd;

    @Column(name = "employee_ad")
    private String employee_ad;

    @Column(name = "employee_sb")
    private String employee_sb;

    @Column(name = "employee_ph")
    private String employee_ph;

    @Column(name = "employee_em")
    private String employee_em;

    @Column(name = "employee_pi")
    private String employee_pi;

    @Column(name = "employee_bs")
    private BigDecimal employee_bs;

    @Column(name = "employee_bk")
    private String employee_bk;

    @Column(name = "employee_an")
    private String employee_an;
    
    @Column(name = "employee_dt")
    private String employee_dt;

    @Column(name = "employee_wr")
    private String employee_wr;

    @Column(name = "employee_wd")
    private Timestamp employee_wd;

    @Column(name = "employee_mf")
    private String employee_mf;

    @Column(name = "employee_md")
    private Timestamp employee_md;
	
	
	// 생성자
	public Employee() {
	}
	
    public Employee(
		    		Long employee_cd
		    		, String employee_id
		    		, String employee_pw
		    		, String employee_dp
		    		, String employee_gd
		    		, String employee_hd
		    		, String employee_rd
		    		, String employee_rr
		    		, String employee_cg
		    		, String employee_nt
		    		, String employee_nm
		    		, String employee_bd
		    		, String employee_ad
		    		, String employee_sb
		    		, String employee_ph
		    		, String employee_em
		    		, String employee_pi
		    		, BigDecimal employee_bs
		    		, String employee_bk
		    		, String employee_an
		    		, String employee_dt
		    		, String employee_wr
		    		, Timestamp employee_wd
		    		, String employee_mf	
		    		, Timestamp employee_md
		    		
    			) {
    	
    	this.employee_cd = employee_cd;
    	this.employee_id = employee_id;
    	this.employee_pw = employee_pw;
    	this.employee_dp = employee_dp;
    	this.employee_gd = employee_gd;
    	this.employee_hd = employee_hd;
    	this.employee_rd = employee_rd;
    	this.employee_rr = employee_rr;
    	this.employee_cg = employee_cg;
    	this.employee_nt = employee_nt;
    	this.employee_nm = employee_nm;
    	this.employee_bd = employee_bd;
    	this.employee_ad = employee_ad;
    	this.employee_sb = employee_sb;
    	this.employee_ph = employee_ph;
    	this.employee_em = employee_em;
    	this.employee_pi = employee_pi;
    	this.employee_bs = employee_bs;
    	this.employee_bk = employee_bk;
    	this.employee_an = employee_an;
    	this.employee_dt = employee_dt;
    	this.employee_wr = employee_wr;
    	this.employee_wd = employee_wd;
    	this.employee_mf = employee_mf;	
    	this.employee_md = employee_md;

    	
     
    }

    public static Employee setEmployeeEntity(EmployeeDTO employeeDto) {
        Employee employee = new Employee();
        employee.setEmployee_cd(employeeDto.getEmployee_cd());
        employee.setEmployee_id(employeeDto.getEmployee_id());
        employee.setEmployee_pw(employeeDto.getEmployee_pw());
        employee.setEmployee_dp(employeeDto.getEmployee_dp());
        employee.setEmployee_gd(employeeDto.getEmployee_gd());
        employee.setEmployee_hd(employeeDto.getEmployee_hd());
        employee.setEmployee_rd(employeeDto.getEmployee_rd());
        employee.setEmployee_rr(employeeDto.getEmployee_rr());
        employee.setEmployee_cg(employeeDto.getEmployee_cg());
        employee.setEmployee_nt(employeeDto.getEmployee_nt());
        employee.setEmployee_nm(employeeDto.getEmployee_nm());
        employee.setEmployee_bd(employeeDto.getEmployee_bd());
        employee.setEmployee_ad(employeeDto.getEmployee_ad());
        employee.setEmployee_sb(employeeDto.getEmployee_sb());
        employee.setEmployee_ph(employeeDto.getEmployee_ph());
        employee.setEmployee_em(employeeDto.getEmployee_em());
        employee.setEmployee_pi(employeeDto.getEmployee_pi());
        employee.setEmployee_bs(employeeDto.getEmployee_bs());
        employee.setEmployee_bk(employeeDto.getEmployee_bk());
        employee.setEmployee_an(employeeDto.getEmployee_an());
        employee.setEmployee_dt(employeeDto.getEmployee_dt());
        employee.setEmployee_wr(employeeDto.getEmployee_wr());
        employee.setEmployee_wd(employeeDto.getEmployee_wd());
        employee.setEmployee_mf(employeeDto.getEmployee_mf());
        employee.setEmployee_md(employeeDto.getEmployee_md());


        return employee;
    }
    
	public static Employee createEmployee(EmployeeDTO employeeDto) {

		return new Employee(
				employeeDto.getEmployee_cd()
				, employeeDto.getEmployee_id()
				, employeeDto.getEmployee_pw()
				, employeeDto.getEmployee_dp()
				, employeeDto.getEmployee_gd()
				, employeeDto.getEmployee_hd()
				, employeeDto.getEmployee_rd()
				, employeeDto.getEmployee_rr()
				, employeeDto.getEmployee_cg()
				, employeeDto.getEmployee_nt()
				, employeeDto.getEmployee_nm()
				, employeeDto.getEmployee_bd()
				, employeeDto.getEmployee_ad()
				, employeeDto.getEmployee_sb()
				, employeeDto.getEmployee_ph()
				, employeeDto.getEmployee_em()
				, employeeDto.getEmployee_pi()
				, employeeDto.getEmployee_bs()
				, employeeDto.getEmployee_bk()
				, employeeDto.getEmployee_an()
				, employeeDto.getEmployee_dt()
				, employeeDto.getEmployee_wr()
				, employeeDto.getEmployee_wd()
				, employeeDto.getEmployee_mf()
				, employeeDto.getEmployee_md());
	}
    
    
}
	
