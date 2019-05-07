package com.yy.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan(basePackages = {"com.yy.store.*"})//扫描的路径
@MapperScan(basePackages = "com.yy.store.mapper")//
public class MainConfig {

}
