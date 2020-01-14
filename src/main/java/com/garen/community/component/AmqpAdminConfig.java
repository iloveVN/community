package com.garen.community.component;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AmqpAdminConfig implements InitializingBean {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private DirectExchange directExchange;

    @Autowired
    private Queue queue;

    @Autowired
    private Binding binding;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 将组件添加到rabbitmq中
        amqpAdmin.declareExchange(directExchange);

        amqpAdmin.declareQueue(queue);

        amqpAdmin.declareBinding(binding);
    }
}
