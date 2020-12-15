package com.mytest.myactivitytest.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/24 10:52
 * @description:
 */
@Component
public class OnlyId {

    private static Snowflake snowflake;

    @PostConstruct
    private static void init(){
        snowflake = IdUtil.getSnowflake(1, 1);
    }

    public static String getId(){
        return String.valueOf(snowflake.nextId());
    }
}
