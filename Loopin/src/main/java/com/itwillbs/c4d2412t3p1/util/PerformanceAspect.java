package com.itwillbs.c4d2412t3p1.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {
	
//	@Around("execution(* com.itwillbs.c4d2412t3p1.service.LogService.select_LOG())")
//    public Object measureTime(ProceedingJoinPoint joinPoint) throws Throwable {
//        long startTime = System.nanoTime();
//
//        Object result = joinPoint.proceed();
//
//        long duration = System.nanoTime() - startTime;
//        double durationMs = duration / 1_000_000.0;
//        System.out.println(joinPoint.getSignature() + " 실행시간(ms): " + durationMs);
//
//        return result;
//    }
	
}
