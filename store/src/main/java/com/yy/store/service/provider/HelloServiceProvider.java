package com.yy.store.service.provider;

import com.alibaba.dubbo.config.annotation.Service;
import com.yy.store.service.api.HelloServiceApi;
import org.springframework.web.bind.annotation.RequestParam;

@Service(                      //对外提供的接口我们就使用阿里的，而不是spring的
        version = "1.0.0",
        application = "${dubbo.application.id}",
        protocol = "${dubbo.protocol.id}",
        registry = "${dubbo.registry.id}"
)
public class HelloServiceProvider implements HelloServiceApi{
    @Override
    public String sayHello(@RequestParam("name") String name) {
        System.out.println("--------name:" + name);
        return "hello " + name;
    }
}
