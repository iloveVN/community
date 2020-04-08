package com.garen.community.service.impl;

import com.garen.community.domain.User;
import com.garen.community.mapper.UserMapper;
import com.garen.community.service.UserSerivce;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 *
 *
 * 使用缓存的步骤： 1. 开启注解缓存，使用@EnableCaching注解开启注解缓存
 *                 2. 使用缓存注解： 包括@Cacheable，@CacheEvict，@CachePut
 * @Cacheable: 将方法的运行结果进行缓存，以后再要相同的数据，直接从缓存中获取，不需要再从数据库获取
 * 属性:
 *  CacheManager管理多个Cache组件，对缓存的真正CRUD操作在Cache组件中，每一个缓存组件有自己唯一的名字
 *  cacheNames/value: 指定缓存组件的名称，将方法的返回结果放在哪个缓存中，是数组的方式，可以指定多个缓存
 *  key: 因为缓存数据的结构是key-value，缓存是使用的key就可以通过此属性来指定。 默认是使用方法参数的值，
 *          即此时的key-value表示为:   方法参数的值-方法返回值.
 *       key值可以通过SpEL表达式指定。
 *  keyGenerator: key的生成器， 可以自己指定key的生成器组件id
 *      key和keyGenerator 两者只能同时存在一个。
 *  cacheManager: 缓存管理器
 *  cacheResolver：缓存解析器
 *          cacheResolver和cacheManager: 二选一
 *  condition： 指定符合条件的情况下才缓存。
 *          condition = "#id>1" 第一个参数的值>1的时候才进行缓存
 *  unless: 当unless指定的条件为true，则不会缓存。
 *  sync: 是否使用异步模式，默认情况下是 false-同步方式，将方法执行完，将方法结果缓存在对象中
 *          如果使用true-异步方式， 则属性unless将不再支持。
 *
 *  原理:
 *      1. 自动配置类: CacheAutoConfiguration
 *      2. 缓存的配置类
 *          org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
 *          org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration
 *          org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration
 *          org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration
 *          org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration
 *          org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration
 *          org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 *          org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration
 *          org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration
 *          org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration
 *      3. 哪个配置类默认生效 :
 *          SimpleCacheConfiguration
 *              作用: 给容器中注册了一个CacheManager: ConcurrentMapCacheManager
 *              可以获取和创建ConcurrentMapCache类型的缓存组件；它的作用是将数据保存在ConcurrentMap中
 *
 *   运行流程
 *      @Cacheable
 *      1. 方法运行之前，先去查询Cache(缓存组件)， 按照cacheNames指定的名字获取；
 *          CacheManager先获取相应的缓存，第一次获取缓存如果没有Cache组件会自动创建
 *      2. 去Cache中查询缓存的内容，使用一个key，默认就是方法的参数;
 *          key是按照某种策略生成的，默认是使用KeyGenerator生成的，默认是使用SimpleKeyGenerator生成
 *              SimpleKeyGenerator生成key的默认策略
 *                  1. 如果没有参数 key = new SimpleKey();
 *                  2. 如果有一个参数 key = 参数的值;
 *                  3. 如果有多个参数 key = new SimpleKey(params);
 *      3. 没有查询到缓存就调用目标方法
 *      4. 将目标方法的返回值，放进缓存中                          ------ 这种很像AOP的原理
 *
 *      @Cacheable标注的方法执行之前，先检测缓存中是否存在数据，默认按照这个方法的参数值作为key去查询缓存，
 *          如果没有就运行方法并将结果放入缓存中。之后再次查询就会从换从中获取.
 *   核心；
 *      1. 使用CacheManager【ConcurrentMapCacheManager】缓存管理器按照名字得到Cache【ConcurrentMapCache】缓存组件
 *      2. key使用KeyGenerator生成的，默认是使用SimpleKeyGenerator
 *
 *
 */

/**
 * @CacheConfig 抽取缓存的公共配置
 */
@CacheConfig(cacheNames = "user")
@Service
public class UserServiceImpl implements UserSerivce {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * @Cacheable注解
     * @param id
     * @throws Exception
     */
    @Cacheable(value = "user", key = "#root.args[0]", /*keyGenerator = "myKeyGenerator",*/ condition = "#id>5"/*, unless = "#result eq null and #id==6 "*/)
    @Override
    public User getUser(Integer id) {
        return userMapper.getUser(id);
    }


    /**
     * CachePut注解是在方法执行之后，才会执行，也就是说方法体中的代码一定会执行
     * 要想使用CachePut注解，首先key值要与查询的key值一致，其次要保证必须存在返回值
     * @param user
     * @throws Exception
     */
    @CachePut(cacheNames = {"user"}, key= "#user.id")
    @Override
    public User updateUserById(User user) {
        userMapper.updateUser(user);
        return user;
    }

    @Override
    public void saveUser(User user) {
        userMapper.saveUser(user);
    }

    /**
     *  根据名称获取用户信息
     *  如果缓存存在，从缓存中获取地址信息
     *  如果缓存不存在，从 DB 中获取地址信息，然后插入缓存
     *  默认的如果key键存在:,则冒号前面的标识namespace，冒号后面的标识key值
     * @param username
     * @return
     */
    @Override
    public User getUserByName(String username) {

        // 从缓存中获取用户信息
        String key = "user:" + username;
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
    @Transactional
    public void updateUser(User user) {
        logger.info("更新用户start...");
        userMapper.updateUser(user);
        // 缓存存在，删除缓存
        String key = "user:" + user.getUsername();
        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            redisTemplate.delete(key);
            logger.info("更新用户时候，从缓存中删除用户 >> " + user.getUsername());
        }
    }



    /**
     * @CacheEvict 缓存清除
     *      key: 指定要清除的缓存数据
     *      allEntries = true : 指定清除这个缓存组件中所有的数据
     *      beforeInvocation = true : 缓存的清除是否在方法之前执行
     *          默认值为false，表示缓存的清除在方法执行之后执行， 如果出现异常，缓存就不会被清除
     *                  true, 缓存的清除是否在方法之前执行, 无论方法是否出现异常，缓存都会清除
     * @param id
     * @throws Exception
     */
    @CacheEvict(cacheNames = {"user"}, key="#id")
    @Override
    public void deleteUser(Integer id) {
        System.out.println("缓存值删除了。。。。");
    }
}
