package com.mytest.myactivitytest.mapper;

import com.mytest.myactivitytest.entity.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/26 9:20
 * @description:
 */
@Repository
public interface WindowTestMapper {

    // 根据 ID 查询
    @Select("select * from student where id = #{id}")
    Student getById(Serializable id);

    @Insert("insert into student values('2','小绿','18') a a  a ")
    Integer insertStudent();
}
