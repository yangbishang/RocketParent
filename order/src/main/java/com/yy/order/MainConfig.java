package com.yy.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ComponentScan(basePackages = {"com.yy.order.*"})//扫描的路径
@MapperScan(basePackages = "com.yy.order.mapper")//
public class MainConfig {

}
