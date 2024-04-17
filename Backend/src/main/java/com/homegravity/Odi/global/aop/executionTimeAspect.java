package com.homegravity.Odi.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class executionTimeAspect {

    @Around("execution(* com..domain.*.controller.*.*(..))")
    public Object logControllerMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, "\u001B[92m", "------");
    }

    @Around("execution(* com..domain.*.service.*.*(..))")
    public Object logServiceMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, "\u001B[96m", "----");
    }

    @Around("execution(* com..domain.*.repository.*.*(..))")
    public Object logRepositoryMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        return logExecutionTime(joinPoint, "\u001B[94m", "--");
    }

    private Object logExecutionTime(ProceedingJoinPoint joinPoint, String color, String methodType) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        log.info("{}{}/{} 종료 ==> 실행 시간: {}ms", color, methodType, joinPoint.getSignature().getName(), executionTime);
        return result;
    }
}
