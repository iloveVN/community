package com.garen.community.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRabbitMQConfig {

    @Bean
    public DirectExchange createExchange() {
        return new DirectExchange("test.amqpexchange");
    }

    @Bean
    public Queue createQueue() {
        return new Queue("test.amqpqueue", true);
    }

    /**
     * 编程式的绑定，也可以通过AmqpAdmin对象进行队列、交换机创建和绑定
     * @return
     */
    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(createQueue()).to(createExchange()).with("test.amqprouting");
    }
}
