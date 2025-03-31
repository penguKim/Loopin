package com.itwillbs.c4d2412t3p1.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryPerformanceAspect {

	// repository 패키지 내의 모든 메서드를 대상으로 한다.
//    @Around("execution(* com.itwillbs.c4d2412t3p1.repository..*(..))")
//    public Object measureRepositoryCall(ProceedingJoinPoint joinPoint) throws Throwable {
//        long startTime = System.nanoTime();
//        
//        // 실제 repository 메서드 실행
//        Object result = joinPoint.proceed();
//        
//        long duration = System.nanoTime() - startTime;
//        double durationMs = duration / 1_000_000.0;
//        
//        System.out.println("[RepositoryTimingAspect] " + joinPoint.getSignature() 
//            + " 실행시간: " + durationMs + "ms");
//        
//        return result;
//    }
}
