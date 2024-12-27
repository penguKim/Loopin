package com.itwillbs.c4d2412t3p1.entity;

import java.sql.Timestamp;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.itwillbs.c4d2412t3p1.domain.MemberDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Entity
@Table(name = "members")
@Getter
@Setter
@ToString
public class Member {
	
	@Id
	@Column(name = "id", length = 50)
	private String id;
	@Column(name = "pass", nullable = false)
	private String pass;
	@Column(name = "name")
	private String name;
	@Column(name = "dates")
	private Timestamp dates;
	
	// spring security 권한 컬럼 추가 => 일반사용자 USER, 관리자 ADMIN로 설정
	@Column(name = "role")
	private String role;
	
	// 스프링 시큐리티 : 애플리케이션 인증, 인가를 일관된 형태로 처리하는 모듈
	// 인증 : 로그인 사용자 식별
	// 인가 : 시스템 자원에 대한 접근을 통제
	
	// SecurityFilterChain -> 인증, 인가 처리
	
	// 생성자
	public Member() {
	}
	
	public Member(String id, String pass, String name, String roleUser, Timestamp date) {
		this.id = id;
		this.pass = pass;
		this.name = name;
		this.role = roleUser;
		this.dates = date;
	}
	
	public static Member setMemberEntity(MemberDTO memberDTO) {
		Member member = new Member();
		member.setId(memberDTO.getId());
		member.setPass(memberDTO.getPass());
		member.setName(memberDTO.getName());
		member.setDates(memberDTO.getDates());
		member.setRole(memberDTO.getRole()); // 추가
		
		return member;
	}
	
	// 회원가입
	// DTO와 암호화를 위한 PasswordEncoder 클래스를 파라미터로 받는다.
	public static Member createUser(MemberDTO memberDTO, PasswordEncoder passwordEncoder) {
		String roleUser = null;
		if(memberDTO.getId().equals("admin")) {
			roleUser = "ADMIN";
		} else {
			roleUser = "USER";
		}

		return new Member(memberDTO.getId(), passwordEncoder.encode(memberDTO.getPass()), 
				memberDTO.getName(), roleUser, new Timestamp(System.currentTimeMillis()));
	}
	
}
