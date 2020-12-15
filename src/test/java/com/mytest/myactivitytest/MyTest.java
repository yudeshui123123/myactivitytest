package com.mytest.myactivitytest;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/12/1 15:20
 * @description:
 */
@SpringBootTest
public class MyTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    /**
     * 部署流程
     * 测试请假流程到了变量分支，两个条件都为true，加入排他网关
     * 当任务执行前，把执行人放入到assagin
     */
    @Test
    void test01() {
        //1.部署流程myHoliday02.bpmn
        repositoryService.createDeployment()
                .addClasspathResource("bpmn/myHoliday02.bpmn")
                .name("排他网关测试流程")
                .deploy();

        //2.启动任务
        ProcessInstance myHoliday02 = runtimeService.startProcessInstanceByKey("myHoliday02", "1");

        //3.给任务赋值执行人，赋值全局变量
        Task task = taskService.createTaskQuery()
                .processInstanceId(myHoliday02.getId()).singleResult();
        //4.aa填写请假单
        if (task != null) {
            taskService.setAssignee(task.getId(), "aa");
            taskService.complete(task.getId());
        }

        //5.根据候选人查询，当前用户是否是候选人，并且让候选人xiaoa拾取任务，然后放弃任务，让xiaob拾取任务，然后转交给xiaoc
        Task xiaoa = taskService.createTaskQuery()
                .processInstanceBusinessKey("1")
                .taskCandidateUser("xiaoa")
                .singleResult();
        if (xiaoa != null) {
            //使用test测试是否能查询成功
            Task test = taskService.createTaskQuery()
                    .processInstanceBusinessKey("1")
                    .taskCandidateUser("test")
                    .singleResult();

            //如果不等于null，则xiaoa是候选人之一,拾取任务
            taskService.claim(xiaoa.getId(), "xiaoa");
            //然后放弃任务
            taskService.setAssignee(xiaoa.getId(), null);

            //xiaob拾取任务
            Task xiaob = taskService.createTaskQuery()
                    .processInstanceBusinessKey("1")
                    .taskCandidateUser("xiaob")
                    .singleResult();
            if (xiaob != null) {
                taskService.claim(xiaob.getId(), "xiaob");
                taskService.setAssignee(xiaob.getId(), null);
                //转交给xiaoc,转交之前得先放弃任务，不然会抛异常
                taskService.claim(xiaob.getId(), "xiaoc");
                //根据xiaoc查询任务,测试是否能查询的到
                Task xiaoc = taskService.createTaskQuery()
                        .processInstanceBusinessKey("1")
                        .taskAssignee("xiaoc")
                        .singleResult();
                if (xiaoc != null) {
                    //完成任务，并设置num的值为5天
                    taskService.complete(xiaoc.getId(), new HashMap<String, Object>() {{
                        put("num", "5");
                    }});
                }
            }
        }

        //根据daa候选人查询是否有任务
        Task daa = taskService.createTaskQuery().processInstanceBusinessKey("1").taskCandidateUser("daa").singleResult();
        if (daa != null) {
            System.out.println("daa查询到了任务");
        }
    }

    /**
     * 并行网关测试
     */
    @Test
    void test02() {
        //1.部署流程
        repositoryService.createDeployment()
                .addClasspathResource("bpmn/myHoliday03.bpmn")
                .name("并行网关测试流程")
                .deploy();
        //2.启动流程实例，并设置变量,并让aa填写请假单
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myHoliday03", "2",
                new HashMap<String, Object>() {{
                    put("num", "5");
                }});
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        taskService.setAssignee(task.getId(), "aa");
        taskService.complete(task.getId());
        //3.xiaoa拾取任务并执行
        Task xiaoa = taskService.createTaskQuery()
                .processInstanceBusinessKey("2")
                .taskCandidateUser("xiaoa")
                .singleResult();
        if (xiaoa != null) {
            taskService.claim(xiaoa.getId(), "xiaoa");
            taskService.complete(xiaoa.getId());
        }
        //4.daa拾取任务并执行
        Task daa = taskService.createTaskQuery()
                .processInstanceBusinessKey("2")
                .taskCandidateUser("daa")
                .singleResult();
        if (daa != null) {
            taskService.claim(daa.getId(), "daa");
            taskService.complete(daa.getId());
        }
        Task task2 = taskService.createTaskQuery()
                .processInstanceBusinessKey("2")
                .singleResult();
        if (task2 != null) {
            //人事存档
            taskService.setAssignee(task2.getId(), "人事经理");
            taskService.complete(task2.getId());
        }
        //5.根据业务id查询到任务的集合，循环完成节点任务并设置，执行人是谁。
        List<Task> list = taskService.createTaskQuery()
                .processInstanceBusinessKey("2").list();
        for (Task task1 : list) {
            if (task1 != null) {
                taskService.setAssignee(task1.getId(), "也是会计也是行政考勤人员");
                taskService.complete(task1.getId());
            }
        }
    }

    @Test
    void test03() {
        //5.根据业务id查询到任务的集合，循环完成节点任务并设置，执行人是谁。
        List<Task> list = taskService.createTaskQuery()
                .processInstanceBusinessKey("2").list();
        for (Task task1 : list) {
            if (task1 != null) {
                taskService.setAssignee(task1.getId(), "也是会计也是行政考勤人员");
                taskService.complete(task1.getId());
            }
        }
    }

    @Test
    void test04() {
        //部署流程
//        Deployment deployment = repositoryService.createDeployment()
//                .addClasspathResource("bpmn/tijian.bpmn")
//                .name("体检流程")
//                .deploy();
        //启动流程
        ProcessInstance tijian01 = runtimeService.startProcessInstanceByKey("myProcess_1","5");

        //查询任务,设置任务的变量值,执行流程
        Task task = taskService.createTaskQuery().processInstanceId(tijian01.getId()).singleResult();
        if (task != null) {
            task.setAssignee("xiaohong");
            taskService.setAssignee(task.getId(),"xiaohong");
            Map<String, Object> map = new HashMap<String, Object>() {
                {
                    put("userType", "1");
                }
            };
            taskService.complete(task.getId(),map);
        }
        //查询多条任务，并执行
        List<Task> list = taskService.createTaskQuery().processInstanceBusinessKey("5").list();
        for (Task task1 : list) {
            taskService.setAssignee(task1.getId(),"xiaohong");
            taskService.complete(task1.getId());
        }
        //再次查询，在执行
        Task task1 = taskService.createTaskQuery().processInstanceBusinessKey("5").singleResult();
        if(task1 != null){
            taskService.setAssignee(task1.getId(),"xiaohong");
            taskService.complete(task1.getId());
        }
        //流程结束
    }
}
