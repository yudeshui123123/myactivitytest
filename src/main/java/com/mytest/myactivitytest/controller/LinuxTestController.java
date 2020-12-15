package com.mytest.myactivitytest.controller;

import com.mytest.myactivitytest.entity.Student;
import com.mytest.myactivitytest.service.LinuxTestService;
import com.mytest.myactivitytest.service.QueryByIdService;
import com.mytest.myactivitytest.service.WindowTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/26 9:24
 * @description:
 */
@RestController
@RequestMapping("/linux")
public class LinuxTestController {

    @Autowired
    private LinuxTestService linuxTestService;

    @Autowired
    private QueryByIdService queryByIdService;

    @Autowired
    private WindowTestService windowTestService;

    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable String id){
        return linuxTestService.getById(id);
    }

    @GetMapping("/getStudent2/{id}")
    public Student getStudent2(@PathVariable String id){
        return queryByIdService.getById(id);
    }

    @GetMapping("/insert")
    public int insert(){
        return linuxTestService.insertStudent();
    }
}
