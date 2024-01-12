package com.rf.librarymanagementsystem.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(1)
public class ExceptionAspect {

    @Pointcut("execution(* com.rf.librarymanagementsystem.controllers..*(..))")
    public void loggingPointcut() {

    }

    @AfterReturning(pointcut = "loggingPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("After returning from {}: {}", joinPoint.getSignature().toShortString(), result);
    }

    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in {}: {}", joinPoint.getSignature().toShortString(), exception.getMessage());
    }
}
