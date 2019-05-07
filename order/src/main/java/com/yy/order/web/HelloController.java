package com.yy.order.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yy.store.service.api.HelloServiceApi;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Reference(
            version = "1.0.0",
            application = "${dubbo.application.id}",
            interfaceName = "com.yy.store.Service.api.HelloServiceApi",
            check = true,  //如果order和store服务是强依赖关系，就true
            timeout = 3000,
            retries = 0  //读请求允许重试3此，写请求不要进行重试
    )
    private HelloServiceApi helloServiceApi;

    @RequestMapping("/hello")
    public String hello(@RequestParam String name) {
        System.out.println("--------2");
        return helloServiceApi.sayHello(name);
    }
}
