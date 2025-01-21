package com.itwillbs.c4d2412t3p1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/upload/**") // 요청 경로
//                .addResourceLocations("file:///C:/Users/ITWILL/git/Loopin/Loopin/src/main/resources/static/upload/"); // 실제 파일 경로
        registry.addResourceHandler("/upload/**") // 요청 경로
        .addResourceLocations("/upload"); // 실제 파일 경로
    }
}