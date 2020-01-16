package com.garen.community.component;

import com.garen.community.domain.User;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class RabbitTest {

    @RabbitHandler
    @RabbitListener(queues = "test.amqpqueue")
    public void testRabbitListener(@Payload Message message) {
        System.out.println(message.getBody());
    }


    // 如果同时存在两个不同的方法监听一个，
    @RabbitHandler
    @RabbitListener(queues = "test.amqpqueue")
    public void testRabbitListenerJSON(@Payload User user) {
        System.out.println(user);
    }

}
