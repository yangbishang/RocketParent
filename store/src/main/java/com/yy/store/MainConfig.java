package com.yy.store;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Configuration
@MapperScan(basePackages = "com.yy.store.mapper")
@ComponentScan(basePackages = {"com.yy.store.*", "com.yy.store.config.*"})
public class MainConfig {

}
