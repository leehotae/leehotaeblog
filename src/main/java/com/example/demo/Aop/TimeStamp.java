package com.example.demo.Aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class TimeStamp

{
	
	
    @Around("execution(* com.example.demo.*.*.*(..))")
    public Object executionAspect(ProceedingJoinPoint joinPoint) throws Throwable
    {   
    	
    	 Logger logger = LoggerFactory.getLogger(getClass());

    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .currentRequestAttributes())
                .getRequest();
      
        long start = System.currentTimeMillis();

        Object value;

        try {
            value = joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        } finally {
            long duration = System.currentTimeMillis() - start;

            logger.info("{} {} from {} [{}] took {} ms",
                    request.getMethod(), request.getRequestURI(),
                    request.getRemoteAddr(),joinPoint.getSignature().toShortString() , duration);
      
        }

        return value;
    }
}