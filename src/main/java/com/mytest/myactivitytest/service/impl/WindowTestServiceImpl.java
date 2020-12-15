package com.mytest.myactivitytest.service.impl;

import com.mytest.myactivitytest.entity.Student;
import com.mytest.myactivitytest.mapper.WindowTestMapper;
import com.mytest.myactivitytest.service.WindowTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/26 9:18
 * @description:
 */
@Service
@Transactional
public class WindowTestServiceImpl implements WindowTestService {

    @Autowired
    private WindowTestMapper windowTestMapper;

    @Override
    public Student getById(String id) {
        return windowTestMapper.getById(id);
    }

    @Override
    public Integer insertStudent() {
        return windowTestMapper.insertStudent();
    }
}
