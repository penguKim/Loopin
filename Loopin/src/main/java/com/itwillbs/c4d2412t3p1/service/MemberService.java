package com.itwillbs.c4d2412t3p1.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@RequiredArgsConstructor
@Service
@Log
public class MemberService {
	
	private final MemberMapper memberMapper;
	
	public List<Map<String, Object>> memberList() {
		log.info("매퍼 접근할겁니다!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		return memberMapper.selectMembers();
	}
	

}
