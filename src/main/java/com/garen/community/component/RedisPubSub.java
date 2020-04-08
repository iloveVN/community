package com.garen.community.component;


import com.garen.community.domain.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 通过redis推送消息
 */
@Component
public class RedisPubSub implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(RedisPubSub.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${demo.topic}")
    private ChannelTopic topic;

    @Autowired
    private RedisMessageListenerContainer msgListenerContainer;

    /**
     * second(秒)，minute(分), hour(时), day of month(日), month(月), day of week(周几),
     *
     * 0 * * * * MON-FRI
     *
     *  [0 0/5 14,18 * * ?] 每天的14,18点每个5分钟执行一次
     *  [0 15 10 ? * 1-6] 每个月的星期一至星期六10:15分执行一次
     *  [0 0 2 ? * 6L] 每个月的最后一个周六凌晨2点执行一次
     *  [0 0 2 LW * ?]  每个月的最后一个工作日凌晨两点执行一次
     *  [0 0 2-4 ? * 1#1] 每个月的第一个周一凌晨2点到4点期间，每个整点执行一次
     *
     *
     *  字段              允许值                     允许的特殊符号
     *  秒                0-59                           ,-*\/
     *  分                0-59                           ,-*\/
     *  时                0-23                           ,-*\/
     *  日期              1-31                           ,-*\/ L W C
     *  月份              1-12                           ,-*\/
     *  星期              0-7或SUN-SAT,0和7都是SUN        ,-*\/ L C #
     *
     * 特殊字符           代表含义
     *  ,(逗号)           枚举
     *  -                 区间
     *  *                 任意
     *  \/（斜杠）         步长
     *  ？                日/或星期冲突匹配
     *  L                   最后
     *  W                   工作日
     *  C                   和Calendar联系后计算过的值
     *  #                   星期，4#2  第2个星期四
     */
    @Scheduled(cron = "0 0 11 ? * 1-6")
    private void schedule() {
        logger.info("publish message");
        publish("admin", "hey you must go now!");
    }


    public void publish(String publisher, String content) {
        logger.info("message send {} by {} topic {}", content, publisher, topic.getTopic());

        SimpleMessage pushMsg = new SimpleMessage();
        pushMsg.setContent(content);
        pushMsg.setPublisher(publisher);
        pushMsg.setCreatetime(new Date());

        redisTemplate.convertAndSend(topic.getTopic(), pushMsg);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        msgListenerContainer.addMessageListener(new MessageSubscriber(), topic);
    }
}
