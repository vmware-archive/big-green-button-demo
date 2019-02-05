package com.pivotal.demo.FussballService.config;

import io.micrometer.core.instrument.Metrics;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.interceptor.PerformanceMonitorInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableAspectJAutoProxy
@Aspect
public class AopPerformanceConfiguration {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // EXAMPLE: PERFORMANCE MONITOR INTERCEPTOR LOGGING

    /**
     * This will cause PerformanceMonitorInterceptor to trace log every entity repository method execution time.
     * Note that the log level for org.springframework.aop.interceptor.PerformanceMonitorInterceptor must be set
     * to TRACE to see the results.
     */
    @Pointcut("execution(* com.pivotal.demo.FussballService.repo.*.*(..))")
    public void monitor() {
    }

    @Bean
    public PerformanceMonitorInterceptor performanceMonitorInterceptor() {
        return new PerformanceMonitorInterceptor(false);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.pivotal.demo.FussballService.config.AopPerformanceConfiguration.monitor()");
        return new DefaultPointcutAdvisor(pointcut, performanceMonitorInterceptor());
    }

    // EXAMPLE: MICROMETER METRICS

    /**
     * This will fire a Micrometer timer metric for every match repository method execution.
     *
     * @param joinPoint the join point
     * @return the method return value
     * @throws Throwable on failure
     */
    @Around("execution(* com.pivotal.demo.FussballService.repo.MatchRepository.*(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object o = joinPoint.proceed();
        long end = System.currentTimeMillis();
        Metrics.timer("app.fussball.match.jdbc.query.time").record(end - start, TimeUnit.MILLISECONDS);
        return o;
    }
}
