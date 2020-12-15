package com.mytest.myactivitytest.service.impl;

import com.mytest.myactivitytest.entity.Student;
import com.mytest.myactivitytest.mapper.JdbcMapper;
import com.mytest.myactivitytest.mapper.LinuxTestMapper;
import com.mytest.myactivitytest.mapper.OracleMapper;
import com.mytest.myactivitytest.mapper.WindowTestMapper;
import com.mytest.myactivitytest.service.LinuxTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/26 9:24
 * @description:
 */
@Service
@Transactional
public class LinuxTestServiceImpl implements LinuxTestService {

    @Autowired
    private LinuxTestMapper linuxTestMapper;

    @Autowired
    private WindowTestMapper windowTestMapper;

    @Autowired
    private JdbcMapper jdbcMapper;

    @Autowired
    private OracleMapper oracleMapper;

    @Override
    public Student getById(String id) {
        Student byId = windowTestMapper.getById(id);
        return linuxTestMapper.getById(id);
    }
    @Override
    public Integer insertStudent() {
        Integer integer1 = linuxTestMapper.insertStudent();
        Integer integer = jdbcMapper.insertStudent();
        Integer integer2 = oracleMapper.insertStudent();
        return windowTestMapper.insertStudent();
    }
}
