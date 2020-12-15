package com.mytest.myactivitytest.config;

import com.mytest.myactivitytest.common.CustomerContextHolder;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * TODO
 *
 * @author yds
 * @version 1.0
 * @date 2020/11/25 20:29
 * @description:
 */
public class DataSourceConfig extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return CustomerContextHolder.getCustomerType();
    }
}
