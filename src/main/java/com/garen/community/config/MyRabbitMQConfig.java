package com.garen.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "rabbit.custom")
public class MyRabbitMQConfig {

    private static Logger logger = LoggerFactory.getLogger(MyRabbitMQConfig.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;


    private String exchangeName;

    private String queueNameA;

    private String queueNameB;

    private String routingName;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueueNameA() {
        return queueNameA;
    }

    public void setQueueNameA(String queueNameA) {
        this.queueNameA = queueNameA;
    }

    public String getQueueNameB() {
        return queueNameB;
    }

    public void setQueueNameB(String queueNameB) {
        this.queueNameB = queueNameB;
    }

    public String getRoutingName() {
        return routingName;
    }

    public void setRoutingName(String routingName) {
        this.routingName = routingName;
    }


    /********************************* Direct Exchange Test*****************************************/

    /**
     * 声明Direct交换机 支持持久化
     * @return
     */
    @Bean("directExchange")
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }

    /**
     * 声明一个队列，支持持久化
     * @return
     */
    @Bean("directQueue")
    public Queue directQueue() {
        return QueueBuilder.durable("DIRECT_QUEUE").build();
    }

    /**
     * 编程式的绑定，也可以通过AmqpAdmin对象进行队列、交换机创建和绑定
     * 通过绑定键，将指定队列绑定到一个指定的交换机。
     * @return
     */
    @Bean
    public Binding bindingDirect(@Qualifier("directQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("DIRECT_ROUTING_KEY").noargs(); // noargs: 没有其他参数.
    }

    /********************************* Fanout Exchange Test*****************************************/

    /**
     *  声明fanout交换机， 支持持久化
     * @return
     */
    @Bean("fanoutExchange")
    public FanoutExchange createFanoutExchange() {
        // 是否需要判断如果存在则无需再次创建？
        return new FanoutExchange(exchangeName);
    }

    /**
     *  Fanout queue A
     * @return
     */
    @Bean("fanoutQueueA")
    public Queue fanoutQueueA() {
        return new Queue(queueNameA, true);
    }

    /**
     *  Fanout queue B
     * @return
     */
    @Bean("fanoutQueueB")
    public Queue fanoutQueueB() {
        return new Queue(queueNameB, true);
    }

    /**
     * 编程式的绑定，也可以通过AmqpAdmin对象进行队列、交换机创建和绑定
     *
     * 对于fanout exchange交换器不需要通过路由来绑定队列，因为他会对所有的队列都进行通知， 同时入参类型也必须是FanoutExchange。
     * @param queue
     * @param exchange
     * @return
     */
    @Bean
    public Binding bindingA(@Qualifier("fanoutQueueA") Queue queue, @Qualifier("fanoutExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }

    @Bean
    public Binding bindingB(@Qualifier("fanoutQueueB") Queue queue, @Qualifier("fanoutExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }


    /**
     * 超时队列
     * 消息被拒绝（basic.reject/ basic.nack）并且requeue=false
     * 消息TTL过期（参考：RabbitMQ之TTL（Time-To-Live 过期时间））
     * 队列达到最大长度
     * @return
     */
    @Bean("deadLetterQueue")
    public Queue deadLetterQueue() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", "DIRECT_EXCHANGE");
        arguments.put("x-dead-letter-routing-key", "DIRECT_ROUTING_KEY");
        Queue queue = new Queue("DEAD_LETTER_QUEUE",true,false,false,arguments);
        return queue;
    }

    @Bean
    public Binding  deadLetterBinding(@Qualifier("deadLetterQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("DIRECT_ROUTING_KEY2").noargs();
    }

    /**
     * 优先级队列
     * @return
     */
    /*	@Bean("priorityQueue")*/
    public Queue priorityQueue(){
        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10); //队列的属性参数 有10个优先级别
        Queue queue = new Queue("DEAD_LETTER_QUEUE",true,false,false,arguments);
        return queue;
    }

    /*	@Bean
		public Binding priorityBinding(@Qualifier("priorityQueue") Queue queue, @Qualifier("directExchange") Exchange exchange) {
			return BindingBuilder.bind(queue).to(exchange).with("DIRECT_ROUTING_KEY").noargs();
		}*/


    /**
     * rabbitTemplate.setConfirmCallback()
     * 消息发送到 Broker 后触发回调，确认消息是否到达 Broker 服务器，也就是只确认是否正确到达 Exchange 中
     *
     * rabbitTemplate.setReturnCallback()
     * 通过实现 ReturnCallback 接口，启动消息失败返回，比如路由不到队列时触发回调
     * @return
     */
    @Bean
    public AmqpTemplate amqpTemplate() {
        // 使用jackson 消息转换器
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.setEncoding("UTF-8");

        // 开启returncallback   yml需要配置 publisher-returns : true
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            logger.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", replyCode, replyText, exchange, routingKey);
        });

        // 消息确认  yml需要配置 publisher-confirms: true
        // 设置消息确认会影响并发性能，导致消息掉失。因为每个connection最多支持2048个channel,当channel达到2048时，
        // 会报错org.springframework.amqp.AmqpResourceNotAvailableException: The channelMax limit is reached. Try later。
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                logger.debug("消息发送到exchange成功,id: {}", correlationData.getId());
            } else {
                logger.info("消息发送到exchange失败,原因: {}", cause);
            }
        });

        return rabbitTemplate;
    }


    /**
     * 消息消费者代码实现的主要配置
     * @param connectionFactory
     * @return
     */
    @Bean
    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);             //开启手动 ack
        return factory;
    }



}
