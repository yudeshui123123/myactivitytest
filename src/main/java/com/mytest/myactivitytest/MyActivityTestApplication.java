package com.mytest.myactivitytest;

import org.aspectj.lang.annotation.Aspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
//exclude = {DataSourceAutoConfiguration.class}
@MapperScan("com.mytest.myactivitytest.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableAspectJAutoProxy
public class MyActivityTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyActivityTestApplication.class, args);
    }
}
