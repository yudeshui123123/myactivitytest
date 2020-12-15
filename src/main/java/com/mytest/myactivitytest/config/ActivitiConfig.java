package com.mytest.myactivitytest.config;

import lombok.extern.slf4j.Slf4j;
import oracle.ucp.proxy.annotation.Post;
import org.activiti.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/12/1 15:28
 * @description:
 */
@Configuration
@Slf4j
public class ActivitiConfig {

    private ProcessEngine processEngine = null;

    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;

    @PostConstruct
    public void init(){
        if(processEngine == null){
            processEngine = processEngineConfiguration.buildProcessEngine();
        }
        log.info("activiti初始化成功");
    }

    @Bean
    public RepositoryService repositoryService(){
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(){
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(){
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(){
        return processEngine.getHistoryService();
    }

}
