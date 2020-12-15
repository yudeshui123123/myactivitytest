package com.mytest.myactivitytest.mapper;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/27 19:13
 * @description:
 */
@Repository
public interface OracleMapper {
    @Insert("insert into student values('3','jdbc','18')")
    Integer insertStudent();
}
