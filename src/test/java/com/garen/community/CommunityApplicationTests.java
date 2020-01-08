package com.garen.community;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;

@SpringBootTest
class CommunityApplicationTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        System.out.println(dataSource);
    }

    @Test
    void testRedisTemplate() {
        redisTemplate.opsForValue().set("aaa", "1111");
    }

}
