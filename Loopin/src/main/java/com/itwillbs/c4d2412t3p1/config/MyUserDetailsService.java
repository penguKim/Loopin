package com.itwillbs.c4d2412t3p1.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.itwillbs.c4d2412t3p1.entity.Member;
import com.itwillbs.c4d2412t3p1.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

// LoginPro 메서드 역할
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {

	private final MemberRepository memberRepository;
	
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		
		
		Member member = memberRepository.findById(id).orElseThrow(() -> 
			new UsernameNotFoundException("없는 회원")
		);
		
		return User.builder()
				.username(member.getId())
				.password(member.getPass())
				.roles(member.getRole())
				.build();
	}
}
