package com.garen.community;

import com.garen.community.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        System.out.println(dataSource);
    }

    @Test
    void testRedisTemplate() {
        redisTemplate.opsForValue().set("aaa", "1111");
    }

    @Test
    void testCacheManager() {
        System.out.println(cacheManager);
    }

    @Test
    void testRabbitTemplate() {
        User user = new User();
        user.setId(1);
        user.setSex(1);
        user.setUsername("aaa");
        user.setAddress("nan");
        rabbitTemplate.convertAndSend("test.amqpexchange", "test.amqprouting", user);
    }

    @Test
    void testRabbitTemplateMap() {
       Map<String, Object> map = new HashMap<>();
       map.put("name", "吕高忍");
       map.put("age", 1);
       map.put("choose", true);
       rabbitTemplate.convertAndSend("test.amqpexchange", "test.amqprouting", map);
    }

    @Test
    void testRabbitString() {
        rabbitTemplate.convertAndSend("test.amqpexchange", "test.amqprouting", "ssssss");
    }


}
