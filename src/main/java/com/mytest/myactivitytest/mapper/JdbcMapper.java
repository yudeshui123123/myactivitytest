package com.mytest.myactivitytest.mapper;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/27 9:55
 * @description:
 */
@Repository
public interface JdbcMapper {

    @Insert("insert into student values('4','jdbc','18')")
    Integer insertStudent();

}
