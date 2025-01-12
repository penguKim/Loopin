package com.itwillbs.c4d2412t3p1.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//런타임에 애노테이션 정보를 사용할 수 있도록 설정
@Retention(RetentionPolicy.RUNTIME)
//메서드에만 사용 가능
@Target(ElementType.METHOD)
public @interface LogActivity {
//	로그 제목
	String value() default "";
//	수행 작업
	String action() default "";
}
