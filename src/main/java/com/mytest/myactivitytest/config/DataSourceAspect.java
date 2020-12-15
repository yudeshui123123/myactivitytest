package com.mytest.myactivitytest.config;

import com.mytest.myactivitytest.common.CustomerContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/25 20:38
 * @description:
 */
@Aspect
@Component
public class DataSourceAspect {
    private String defaultDataSource;
    private Map<String, Object> targetDataSources;

    @Pointcut("execution(* com.mytest.myactivitytest.mapper.Linux*.*(..))" +
            "||execution(* com.mytest.myactivitytest.mapper.Query*.*(..))")
    public void linuxTest(){}

    @Pointcut("execution(* com.mytest.myactivitytest.mapper.Window*.*(..))")
    public void windowTest(){}

    @Pointcut("execution(* com.mytest.myactivitytest.mapper.Jdbc*.*(..))")
    public void JdbcTest(){}

    @Pointcut("execution(* com.mytest.myactivitytest.mapper.Oracle*.*(..))")
    public void oracleTest(){}

    @Before("linuxTest()")
    public void doBefore(JoinPoint joinPoint) {
        CustomerContextHolder.setCustomerType("dataSource");
    }

    @Before("windowTest()")
    public void doBefore2(JoinPoint joinPoint) {
        CustomerContextHolder.setCustomerType("dataSource2");
    }

    @Before("JdbcTest()")
    public void doBefore3(JoinPoint joinPoint) {
        CustomerContextHolder.setCustomerType("dataSource3");
    }

    @Before("oracleTest()")
    public void doBefore4(JoinPoint joinPoint) {
        CustomerContextHolder.setCustomerType("oracleDataSource");
    }

    @After("linuxTest()||windowTest()||JdbcTest()||oracleTest()")
    public void doAfterReturning(JoinPoint joinPoint) {
        CustomerContextHolder.clearCustomerType();
    }


    public Map<String, Object> getTargetDataSources() {
        return targetDataSources;
    }

    public void setTargetDataSources(Map<String, Object> targetDataSources) {
        this.targetDataSources = targetDataSources;
    }

    public String getDefaultDataSource() {
        return defaultDataSource;
    }

    public void setDefaultDataSource(String defaultDataSource) {
        this.defaultDataSource = defaultDataSource;
    }
}
