package com.yy.store.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @RequestMapping("/index")
    public String index() throws Exception{
        System.err.println("--------1");
        return "index";
    }
}
