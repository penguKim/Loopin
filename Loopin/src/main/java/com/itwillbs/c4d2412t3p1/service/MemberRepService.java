package com.itwillbs.c4d2412t3p1.service;

import java.sql.Timestamp;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.domain.MemberDTO;
import com.itwillbs.c4d2412t3p1.entity.Member;
import com.itwillbs.c4d2412t3p1.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class MemberRepService {
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
		
	public void save(MemberDTO memberDTO) {
		
		memberDTO.setDates(new Timestamp(System.currentTimeMillis()));
		
		Member member = Member.createUser(memberDTO, passwordEncoder);
		
		memberRepository.save(member);
	}
	
}
