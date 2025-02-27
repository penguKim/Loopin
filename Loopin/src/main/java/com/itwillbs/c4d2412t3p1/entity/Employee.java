package com.itwillbs.c4d2412t3p1.entity;

import java.math.BigDecimal;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.itwillbs.c4d2412t3p1.domain.EmployeeDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "EMPLOYEE")
@Getter
@Setter
@ToString
@SequenceGenerator(name = "em_sequence_generator", sequenceName = "em_sequence", allocationSize = 1)
public class Employee {
	

    @Id
    @Column(name = "employee_cd", length = 15)  // String 타입으로 저장, 길이 조정 가능
    private String employee_cd;  // String으로 변경
    
	@Transient
	private Long sequenceValue; // 시퀀스 값을 저장하기 위한 임시 필드
    
	@PrePersist
	public void prePersist() {
	    if (this.employee_cd == null && this.sequenceValue != null) {
	        String currentYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
	        String formattedSequence = String.format("%04d", this.sequenceValue);
	        this.employee_cd = "EM" + currentYear + "-" + formattedSequence;
	    }
	}
    @Column(name = "employee_id", unique = true, length = 16)
    private String employee_id;
    
    @Column(name = "employee_pw", length = 255)
    private String employee_pw;

    @Column(name = "employee_dp", length = 10)
    private String employee_dp;

    @Column(name = "employee_gd", length = 10)
    private String employee_gd;

    @Column(name = "employee_hd", length = 10)
    private String employee_hd;

    @Column(name = "employee_rd", length = 10)
    private String employee_rd;

    @Column(name = "employee_rr", length = 200)
    private String employee_rr;

    @Column(name = "employee_cg", length = 2)
    private String employee_cg;

    @Column(name = "employee_nt", length = 200)
    private String employee_nt;

    @Column(name = "employee_nm", length = 20)
    private String employee_nm;

    @Column(name = "employee_bd", length = 8)
    private String employee_bd;

    @Column(name = "employee_ad", length = 100)
    private String employee_ad;

    @Column(name = "employee_sb", length = 2)
    private String employee_sb;

    @Column(name = "employee_ph", length = 20)
    private String employee_ph;

    @Column(name = "employee_em", length = 50)
    private String employee_em;

    @Column(name = "employee_pi", length = 100)
    private String employee_pi;

    @Column(name = "employee_bs", length = 20)
    private BigDecimal employee_bs;

    @Column(name = "employee_bk", length = 10)
    private String employee_bk;

    @Column(name = "employee_an", length = 20)
    private String employee_an;
    
    @Column(name = "employee_dt", length = 10)
    private String employee_dt;

    @Column(name = "employee_wr", length = 10)
    private String employee_wr;

    @Column(name = "employee_wd")
    private Timestamp employee_wd;

    @Column(name = "employee_mf", length = 10)
    private String employee_mf;

    @Column(name = "employee_md")
    private Timestamp employee_md;
    
    @Column(name = "employee_mg")
    private Boolean employee_mg;
    
    @Column(name = "employee_rl", length = 10)
    private String employee_rl;
	
    @Column(name = "employee_us", length = 10)
    private Boolean employee_us;
    
    @Column(name = "workinghour_id")
    private String workinghour_id;

	// 생성자
	public Employee() {
	}
	
	// Employee 클래스에 새 생성자 추가
	public Employee(
	    String employee_cd,
	    String employee_id,
	    String employee_pw,
	    String employee_dp,
	    String employee_gd,
	    String employee_hd,
	    String employee_rd,
	    String employee_rr,
	    String employee_cg,
	    String employee_nt,
	    String employee_nm,
	    String employee_bd,
	    String employee_ad,
	    String employee_sb,
	    String employee_ph,
	    String employee_em,
	    String employee_pi,
	    BigDecimal employee_bs,
	    String employee_bk,
	    String employee_an,
	    String employee_dt,
	    String employee_wr,
	    Timestamp employee_wd,
	    String employee_mf,
	    Timestamp employee_md,
	    Boolean employee_mg,
	    String employee_rl,
	    Boolean employee_us,
//	    String workinghour_id,	    
	    Long sequenceValue // 추가된 매개변수
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
	    this.employee_mg = employee_mg;
	    this.employee_rl = employee_rl;
	    this.employee_us = employee_us;
//	    this.workinghour_id = workinghour_id;
	    this.sequenceValue = sequenceValue; // 필드 설정
	}


    public static Employee setEmployeeEntity(Employee employee, EmployeeDTO employeeDto, PasswordEncoder passwordEncode) {
        employee.setEmployee_cd(employeeDto.getEmployee_cd());
        employee.setEmployee_id(employeeDto.getEmployee_id());
        // 비밀번호가 비어있지 않으면 암호화하여 설정
        if (employeeDto.getEmployee_pw() != null && !employeeDto.getEmployee_pw().trim().isEmpty()) {
            employee.setEmployee_pw(passwordEncode.encode(employeeDto.getEmployee_pw())); // 비밀번호 암호화
        } else {
            // 또는 기존 비밀번호를 유지하고 싶다면 아래와 같이 처리 가능
            employee.setEmployee_pw(employee.getEmployee_pw());
        }

        
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
        employee.setEmployee_mg(employeeDto.getEmployee_mg());
        employee.setEmployee_rl(employeeDto.getEmployee_rl());
        employee.setEmployee_us(employeeDto.getEmployee_us());
//        employee.setWorkinghour_id(employeeDto.getWorkinghour_id());
        

        return employee;
    }
    
    public static Employee createEmployee(EmployeeDTO employeeDto, String employee_pi, Long sequenceValue, PasswordEncoder passwordEncoder) {
        System.out.println("createEmployee sequenceValue:" + sequenceValue);

        return new Employee(
            null,  // employee_cd는 @PrePersist에서 설정됨
            employeeDto.getEmployee_id(),
            passwordEncoder.encode(employeeDto.getEmployee_pw()),
            employeeDto.getEmployee_dp(),
            employeeDto.getEmployee_gd(),
            employeeDto.getEmployee_hd(),
            employeeDto.getEmployee_rd(),
            employeeDto.getEmployee_rr(),
            employeeDto.getEmployee_cg(),
            employeeDto.getEmployee_nt(),
            employeeDto.getEmployee_nm(),
            employeeDto.getEmployee_bd(),
            employeeDto.getEmployee_ad(),
            employeeDto.getEmployee_sb(),
            employeeDto.getEmployee_ph(),
            employeeDto.getEmployee_em(),
            employee_pi,
            employeeDto.getEmployee_bs(),
            employeeDto.getEmployee_bk(),
            employeeDto.getEmployee_an(),
            employeeDto.getEmployee_dt(),
            employeeDto.getEmployee_wr(),
            employeeDto.getEmployee_wd(),
            employeeDto.getEmployee_mf(),
            employeeDto.getEmployee_md(),
            employeeDto.getEmployee_mg(),
            employeeDto.getEmployee_rl(),
            employeeDto.getEmployee_us(),
//            employeeDto.getWorkinghour_id(),
            sequenceValue
        );
    }
    
}
	
