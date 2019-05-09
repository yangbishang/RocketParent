package com.yy.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@MapperScan(basePackages = "com.bfxy.order.mapper")
@ComponentScan(basePackages = {"com.yy.order.*", "com.yy.order.config.*"})
public class MainConfig {

}
