package com.mytest.myactivitytest.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class OnlyId {

    private static long workerId = 0;
    private static long datacenterId = 1;
    private static Snowflake snowflake;

    @PostConstruct
    private static void init(){
        try {
            workerId = NetUtil.ipv4ToLong(NetUtil.getLocalhostStr());
            log.info("当前机器的workerId：{}",workerId);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
            workerId = NetUtil.getLocalhostStr().hashCode();
        }finally {
            snowflake = IdUtil.getSnowflake(workerId, datacenterId);
        }

    }

    public static synchronized String getId(){
        return String.valueOf(snowflake.nextId());
    }
}
