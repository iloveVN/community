package com.garen.community.service.impl;

import com.garen.community.domain.User;
import com.garen.community.mapper.UserMapper;
import com.garen.community.service.UserSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserSerivce {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Cacheable(cacheNames = {"usercache"})
    @Override
    public User getUser(Integer id) {
        return userMapper.getUser(id);
    }

    @Override
    public void saveUser(User user) {
        userMapper.saveUser(user);
    }

    /**
     *  根据名称获取用户信息
     *  如果缓存存在，从缓存中获取地址信息
     *  如果缓存不存在，从 DB 中获取地址信息，然后插入缓存
     * @param username
     * @return
     */
    @Override
    public User getUserByName(String username) {

        // 从缓存中获取用户信息
        String key = "username:" + username;
        ValueOperations<String, User> operations = redisTemplate.opsForValue();

        // 缓存存在
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            User user = operations.get(key);
            return user;
        }
        // 缓存不存在，从 DB 中获取
        List<User> appUserList = userMapper.getUserByName(username);

        // 插入缓存, 且设置缓存时间
        if(appUserList.size() > 0){
            operations.set(key, appUserList.get(0), 10, TimeUnit.MINUTES);
        }
        return appUserList.size() > 0 ? appUserList.get(0) : null;
    }

    /**
     * 更新用户
     * 如果缓存存在，删除
     * 如果缓存不存在，不操作
     *
     * @param user 用户
     */
    public void updateUser(User user) {
        logger.info("更新用户start...");
        userMapper.updateUser(user);
        // 缓存存在，删除缓存
        String key = "username:" + user.getUsername();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            logger.info("更新用户时候，从缓存中删除用户 >> " + user.getUsername());
        }
    }
}
