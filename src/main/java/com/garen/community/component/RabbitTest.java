package com.garen.community.component;

import com.garen.community.domain.User;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 通过该测试可知，如果我们使用的队列存在不同类型的数值，
 * 那么可以采用这种方式，将不同类型的数据映射到相应的参数中
 */
@Component
@RabbitListener(queues = "test.amqpqueue")
public class RabbitTest {

   @RabbitHandler
    public void testRabbitListener(String json) {
        System.out.println(json);
    }


    @RabbitHandler
    public void testRabbitListener(User user) {
        System.out.println(user);
    }

    @RabbitHandler
    public void testRabbitListener(Map<String, Object> map) {
        System.out.println(map.get("name"));
    }

}
