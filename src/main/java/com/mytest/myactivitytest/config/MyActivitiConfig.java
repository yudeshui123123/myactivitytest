package com.mytest.myactivitytest.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/22 14:24
 * @description:
 */
@Configuration
public class MyActivitiConfig {


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        return new DruidDataSource();
    }

    /**
     * 多配置一个数据源
     */
    @Bean(name = "dataSource2")
    public DataSource dataSource2(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://127.0.0.1:3306/activiti?serverTimezone=GMT%2B8");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123");
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return druidDataSource;
    }

    /**
     * 多配置一个数据源
     */
    @Bean(name = "dataSource3")
    public DataSource dataSource3(){
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl("jdbc:mysql://192.168.17.131:3306/jdbc?serverTimezone=GMT%2B8");
        druidDataSource.setUsername("root");
        druidDataSource.setPassword("123");
        druidDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return druidDataSource;
    }

    @Bean(name = "oracleDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource dataSource4(){
        return new DruidDataSource();
    }

    @Bean
    @Primary
    public DataSource dataSourceChangeAspect(){
        DataSourceConfig dataSourceAspect = new DataSourceConfig();
        dataSourceAspect.setDefaultTargetDataSource(dataSource());
        dataSourceAspect.setTargetDataSources(new HashMap<Object,Object>(){{
            put("dataSource",dataSource());
            put("dataSource2",dataSource2());
            put("dataSource3",dataSource3());
            put("oracleDataSource",dataSource4());
        }});
        return dataSourceAspect;
    }

    /**
     * 配置@Transactional注解事物
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSourceChangeAspect());
    }


    /**
     * 配置processEngineConfiguration
     * @return
     */
    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(){
        SpringProcessEngineConfiguration springProcessEngineConfiguration = new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setTransactionManager(new JdbcTransactionManager(this.dataSource()));
        springProcessEngineConfiguration.setDataSource(this.dataSource());
        springProcessEngineConfiguration.setDatabaseSchemaUpdate("true");
        return springProcessEngineConfiguration;
    }

    /**
     * 配置一个servlet
     * @return
     */
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),"/druid/*");
        Map map = new HashMap<String,String>(){
            {
                put("loginUsername","admin");
                put("loginPassword","123456");
                put("allow","localhost");//默认允许所有访问
                put("deny","192.168.28.78");
            }
        };
        servletRegistrationBean.setInitParameters(map);
        return servletRegistrationBean;
    }

    /**
     * 配置一个web的过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean WebStatFilter(){
        FilterRegistrationBean<WebStatFilter> webStatFilterFilterRegistrationBean = new FilterRegistrationBean<>(new WebStatFilter());
        Map<String,String> map = new HashMap<String,String>(){
            {
                put("exclusions","*.js,*.css,/druid/*");
            }
        };
        webStatFilterFilterRegistrationBean.setInitParameters(map);
        webStatFilterFilterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return webStatFilterFilterRegistrationBean;
    }

}
