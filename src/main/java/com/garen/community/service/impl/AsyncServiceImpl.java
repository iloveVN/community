package com.garen.community.service.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncServiceImpl {

    /**
     * 告诉spring，这时一个异步方法，需要其自己开启一个线程池调用
     */
    @Async
    public void hello() {

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("处理数据中....");
    }

}
