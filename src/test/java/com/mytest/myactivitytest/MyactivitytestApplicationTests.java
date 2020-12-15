package com.mytest.myactivitytest;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.mytest.myactivitytest.config.OnlyId;
import org.activiti.engine.*;
import org.activiti.engine.repository.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

@SpringBootTest
class MyactivitytestApplicationTests {

    /**
     * repositoryService 主要是操作流程定义的信息
     * runTimeService 主要是操作流程实例的信息
     * taskService 主要是任务的操作
     * historyService 主要是历史的操作
     */
    @Autowired
    private ProcessEngineConfiguration processEngineConfiguration;


    @Test
    void contextLoads() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        System.out.println(processEngine);
    }

    @Test
    void Test01() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("bpmn/myHoliday.bpmn")
                .name("请假流程")
                .deploy();
        System.out.println(deploy.getId());

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("mytest01");

    }

    @Test
    void test02() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        List<ProcessDefinition> mytest01 = processDefinitionQuery.processDefinitionKey("mytest01")
                .orderByProcessDefinitionVersion()
                .desc()
                .list();
        for (ProcessDefinition processDefinition : mytest01) {
            System.out.println(processDefinition.getKey());
            System.out.println(processDefinition.getName());
            repositoryService.deleteDeployment(processDefinition.getDeploymentId());
        }
    }

    @Test
    void test03() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        List<Task> list = taskService.createTaskQuery().processDefinitionKey("mytest01").list();
        for (Task task : list) {
            System.out.println(task.getAssignee());
            task.setAssignee("总经理");
            taskService.complete(task.getId());
        }
    }

    @Test
    void test04() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
        System.out.println(processDefinition.getDiagramResourceName());
        //getDiagramResourceName获取图片，getResourceName获取bomn文件
        try (InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
             OutputStream outputStream = new FileOutputStream("D:\\123.png")
        ) {
            IOUtils.copy(resourceAsStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void test05() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().singleResult();
        processEngine.getRepositoryService()
                .deleteDeployment(processDefinition.getDeploymentId(), true);
        processEngine.getRepositoryService().createDeployment()
                .addClasspathResource("bpmn/myHoliday.bpmn")
                .addClasspathResource("bpmn/myHoliday.png")
                .name("请假流程")
                .deploy();

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("mytest01");

        Task task = processEngine.getTaskService().createTaskQuery()
                .singleResult();
        task.setAssignee("填写单子，还是审批？");
        processEngine.getTaskService().complete(task.getId());
    }

    @Test
    void test06() {
        String id = OnlyId.getId();
        System.out.println(id);
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        runtimeService.startProcessInstanceByKey("mytest01", id);
    }

    /**
     * 挂起和开启全部流程
     */
    @Test
    void test07() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        ProcessDefinition mytest01 = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("mytest01")
                .singleResult();
        if (mytest01.isSuspended()) {
            //true暂停
            repositoryService.activateProcessDefinitionById(mytest01.getId(), true, null);
        } else {
            repositoryService.suspendProcessDefinitionById(mytest01.getId(), true, null);

        }
    }

    /**
     * 挂起和开启单个流程
     */
    @Test
    void test08() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey("1331129630253518848").singleResult();
        if (processInstance.isSuspended()) {
            //true暂停
            runtimeService.activateProcessInstanceById(processInstance.getId());
            System.out.println("流程 " + processInstance.getName() + " 激活");
        } else {
            runtimeService.suspendProcessInstanceById(processInstance.getId());
            System.out.println("流程 " + processInstance.getName() + " 暂停");
        }
        System.out.println(processInstance.getProcessInstanceId());
        System.out.println(processInstance.getId());
    }

    /**
     * 启动流程并动态设置任务审批人
     */
    @Test
    void test09() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        repositoryService.createDeployment()
                .addClasspathResource("bpmn/myHoliday.bpmn")
                .addClasspathResource("bpmn/myHoliday.png")
                .name("请假流程")
                .deploy();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("test1", "test01");
                put("test2", "test02");
                put("test3", "test03");
            }
        };
        ProcessInstance test01 = runtimeService.startProcessInstanceByKey("mytest01", OnlyId.getId(), map);
        System.out.println(test01.getName());
    }

    /**
     * 查询单个人
     * assignee 应该放人员的id
     * processInstanceBusinessKey 放业务表的id
     */
    @Test
    void test10() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Task mytest01 = taskService.createTaskQuery().processDefinitionKey("mytest01")
                .taskAssignee("test01") //根据用户id查询
                .singleResult();
        System.out.println(mytest01.getAssignee());
        System.out.println(mytest01.getProcessInstanceId());
        System.out.println(mytest01.getProcessDefinitionId());
        //根据流程实例id查询流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(mytest01.getProcessInstanceId()).singleResult();
        //获取流程实例中的业务id business
        System.out.println(processInstance.getBusinessKey());

//        //根据流程实例id查询任务
//        Task task = taskService.createTaskQuery()
//                .processInstanceId(mytest01.getProcessInstanceId()).singleResult();
//        System.out.println(task.getAssignee());
    }

    /**
     * 部署流程
     * 注释代码为：在流程未结束的时候，给流程实例赋值变量值
     * 全局变量
     */
    @Test
    void test11() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = processEngine.getTaskService();
        //部署流程
        try (InputStream resourceAsStream = MyactivitytestApplicationTests.class.getClassLoader().getResourceAsStream("bpmn/myHoliday01.zip");
             ZipInputStream zipInputStream = new ZipInputStream(resourceAsStream)) {
            repositoryService.createDeployment()
                    .addZipInputStream(zipInputStream)
                    .name("请假流程01")
                    .deploy();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("test1", "test01");
                put("test2", "test02");
                put("test3", "test03");
                put("test4", "test04");
                //put("num","6");
            }
        };
        String id = OnlyId.getId();
        //启动流程
        ProcessInstance mytest01 = runtimeService.startProcessInstanceByKey("mytest01", id, map);
        //给流程实例设置值
        //runtimeService.setVariable(mytest01.getId(),"num","6");
        System.out.println(mytest01.getProcessInstanceId());
        System.out.println(mytest01.getId());
        //根据流程实例id 查询
        Task task = taskService.createTaskQuery().processInstanceId(mytest01.getId()).singleResult();
        System.out.println(task.getAssignee());
        System.out.println(task.getBusinessKey());
    }

    @Test
    void test12() {
        //执行任务
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //查询刚刚部署并启动的任务，然后执行
        Task task = taskService.createTaskQuery().processInstanceBusinessKey("1333262750897541120").singleResult();
        if (task != null) {
            taskService.complete(task.getId());
        }
    }

    /**
     * 任务完成时，给变量设置值
     * 根据任务id设置变量
     * 全局变量
     */
    @Test
    void test13() {
        //执行任务
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        //查询刚刚部署并启动的任务，然后执行
        Task task = taskService.createTaskQuery().processInstanceBusinessKey("1333301624214523904").singleResult();
        //根据任务id设置变量
        //taskService.setVariable(task.getId(),"num","6");
        Map<String, Object> map = new HashMap<String, Object>() {
            {
                put("num", "6");
            }
        };
        if (task != null) {
            taskService.complete(task.getId(), map);
        }
    }

    /**
     * 组任务
     */
    @Test
    void test14() {
        ProcessEngine processEngine = processEngineConfiguration.buildProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        TaskService taskService = processEngine.getTaskService();
//        repositoryService
//                .createDeployment()
//                .addClasspathResource("bpmn/myHoliday.bpmn")
//                .name("候选人流程1")
//                .deploy();
//        //启动流程
//        ProcessInstance mytest02 = runtimeService.startProcessInstanceByKey("mytest02");

        Task mytest02 = taskService.createTaskQuery()
                .processDefinitionKey("mytest02")
                .singleResult();
        //执行任务
        //taskService.complete(mytest02.getId());
        //查询候选人
        List<Task> xiaoming = taskService.createTaskQuery()
                .taskCandidateUser("xiaoming")
                .list();
        System.out.println(xiaoming);
        //判断当前用户是否有组任务
        Task xiaoming1 = taskService.createTaskQuery().taskId(mytest02.getId()).taskCandidateUser("xiaoming").singleResult();
        if (xiaoming1 != null){
            taskService.claim(mytest02.getId(),"xiaoming");
            System.out.println("拾取任务成功");
            //将组任务设置为null，然后就相当于归还了。
            //taskService.setAssignee(xiaoming1.getId(),null);
            //任务交接
            //taskService.setAssignee(xiaoming1.getId(),"xiaohong");
        }
    }
}
