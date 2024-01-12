package com.rf.librarymanagementsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
@Order(0)
public class LoggingAspect {

    // Sample
    @Around(value = "execution(* com.rf.librarymanagementsystem.controllers..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        log.info("KPI: [method-execution] for: {} withArgs: {} - Execution Time: {} ms.",
                joinPoint.getSignature(),
                Arrays.toString(joinPoint.getArgs()),
                executionTime);

        return result;
    }
}
