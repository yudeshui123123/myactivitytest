package com.mytest.myactivitytest.controller;

import com.mytest.myactivitytest.entity.Student;
import com.mytest.myactivitytest.service.WindowTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/26 9:19
 * @description:
 */
@RestController
@RequestMapping("/window")
public class WindowTestController {

    @Autowired
    private WindowTestService windowTestService;

    @GetMapping("/getStudent/{id}")
    public Student getStudent(@PathVariable String id){
        return windowTestService.getById(id);
    }
}
