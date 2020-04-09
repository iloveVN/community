package com.garen.community.controller;

import com.garen.community.service.impl.AsyncServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试异步任务
 */
@RestController
public class AsyncController {

    @Autowired
    private AsyncServiceImpl asyncService;

    @GetMapping("/async/hello")
    public String hello() {
        asyncService.hello();
        return "success";
    }

}
