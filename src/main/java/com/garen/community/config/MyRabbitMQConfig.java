package com.garen.community.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbit.custom")
public class MyRabbitMQConfig {

    private String exchangeName;

    private String queueName;

    private String routingName;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getRoutingName() {
        return routingName;
    }

    public void setRoutingName(String routingName) {
        this.routingName = routingName;
    }

//    @Bean
//    public DirectExchange createExchange() {
//        return new DirectExchange(exchangeName);
//    }

    @Bean
    public FanoutExchange createFanoutExchange() {
        return new FanoutExchange(exchangeName);
    }

    @Bean
    public Queue createQueue() {
        return new Queue(queueName, true);
    }

    /**
     * 编程式的绑定，也可以通过AmqpAdmin对象进行队列、交换机创建和绑定
     *  对于fanout exchange交换器不需要进行绑定，因为它是对所有队列都添加数据。
     * @return
     */
//    @Bean
//    public Binding bindingDirect() {
//        return BindingBuilder.bind(createQueue()).to(createExchange()).with(routingName);
//    }

    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(createQueue()).to(createFanoutExchange());
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }


}
