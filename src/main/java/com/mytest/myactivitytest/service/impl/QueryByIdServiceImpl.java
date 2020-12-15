package com.mytest.myactivitytest.service.impl;

import com.mytest.myactivitytest.entity.Student;
import com.mytest.myactivitytest.mapper.JdbcMapper;
import com.mytest.myactivitytest.mapper.LinuxTestMapper;
import com.mytest.myactivitytest.service.QueryByIdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/26 14:48
 * @description:
 */
@Service
public class QueryByIdServiceImpl implements QueryByIdService {

    @Autowired
    private LinuxTestMapper linuxTestMapper;

    @Autowired
    private JdbcMapper jdbcMapper;

    @Override
    public Student getById(String id) {
        return linuxTestMapper.getById(id);
    }

    @Override
    public Integer insertStudent() {
        return jdbcMapper.insertStudent();
    }
}
