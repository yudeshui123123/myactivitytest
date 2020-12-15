package com.mytest.myactivitytest.service;

import com.mytest.myactivitytest.entity.Student;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/26 14:47
 * @description:
 */
public interface QueryByIdService {
    // 根据 ID 查询
    Student getById(String id);

    Integer insertStudent();
}
