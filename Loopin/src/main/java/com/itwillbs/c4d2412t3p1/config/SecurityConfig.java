package com.itwillbs.c4d2412t3p1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final MyUserDetailsService myUserDetailsService;
//	 암호화 설정
//	@Bean => 서버 시작 시 객체 생성
	@Bean
	public PasswordEncoder passwordEncoder() {
/*
 BCryptPasswordEncoder : 스프링 시큐리티에서 제공하는 클래스 중 하나
 비밀번호를 암호화하는 데 사용할 수 있는 메서드를 가진 클래스
 
 Bcrypt 해싱 함수를 사용하여 비밀번호를 인코딩해주는 메서드 제공
 저장소에 저장된 비밀번호의 일치 여부를 확인해주는 메서드 제공
 */
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		return http
				.authorizeHttpRequests(authorizeHttpRequestsCustomizer -> 
					authorizeHttpRequestsCustomizer
					.requestMatchers("/login").anonymous()
	                .requestMatchers("/assets/**").permitAll()
	                .requestMatchers("/", "/mapper", "/upload").authenticated()
					.anyRequest() // 어느 요청이든
					.authenticated() // 권한이 적용된다.
						)
				.formLogin(formLoginCustomizer ->
					formLoginCustomizer
					.loginPage("/login")
					.loginProcessingUrl("/loginPro")
					.usernameParameter("id")
					.passwordParameter("pass") 
					.defaultSuccessUrl("/")
					.failureHandler((request, response, exception) -> { // 로그인 실패 시 처리
				        String errorMessage;
				        if (exception instanceof BadCredentialsException) {
				            errorMessage = "아이디 또는 비밀번호가<br>일치하지 않습니다.";
				        } else if (exception instanceof DisabledException) {
				            errorMessage = "비활성화된 계정입니다.";
				        } else if (exception instanceof UsernameNotFoundException) {
				            errorMessage = "존재하지 않는 계정입니다.";
				        } else {
				            errorMessage = "로그인에 실패했습니다.";
				        }
				        
				        request.getSession().setAttribute("loginError", errorMessage);
				        response.sendRedirect("/login");
					})
						)
		        .rememberMe(rememberMe -> // 로그인 정보 기억
		            rememberMe
		            .key("uniqueAndSecret")
		            .rememberMeParameter("remember")
		            .tokenValiditySeconds(7 * 24 * 60 * 60) // 7일
		            .userDetailsService(myUserDetailsService)
		        		)
				.logout(logoutCustomizer ->
					logoutCustomizer
					.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) 
					.logoutSuccessUrl("/login")
	                .invalidateHttpSession(true)
	                .clearAuthentication(true)
						)
				.exceptionHandling(handling -> 
                	handling
                	.accessDeniedHandler((request, response, accessDeniedException) -> {
                		response.sendRedirect("/");
                	})
						)
				.userDetailsService(myUserDetailsService)
				.build();
	}
	
}
