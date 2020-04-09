package com.garen.community;

import com.garen.community.domain.Article;
import com.garen.community.domain.Book;
import com.garen.community.domain.User;
import com.garen.community.repository.BookRepository;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.UpperCase;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collection;
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

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private JestClient jestClient;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void contextLoads() {
        System.out.println(dataSource);
    }

    @Test
    void testRedisTemplate() {
        // redis存储数据
        String key = "name";
        redisTemplate.opsForValue().set(key, "yukong");
        // 获取数据
        String value = (String) redisTemplate.opsForValue().get(key);
        System.out.println("获取缓存中key为" + key + "的值为：" + value);

        User user = new User();
        user.setUsername("yukong");
        user.setSex(18);
        user.setId(11);
        String userKey = "yukong";
        redisTemplate.opsForValue().set(userKey, user);
        User newUser = (User) redisTemplate.opsForValue().get(userKey);
        System.out.println("获取缓存中key为" + userKey + "的值为：" + newUser);
    }

    @Test
    void testCacheManager() {
        Collection<String> cacheNames = cacheManager.getCacheNames();
        System.out.println(cacheNames);

        Cache userCache = cacheManager.getCache("user");
        User user1 = userCache.get("38", User.class);
        System.out.println(user1);

        User user  = (User) redisTemplate.opsForValue().get("user::38");
        System.out.println(user);
    }

    @Test
    void createExchange() {
        // 创建交换器
//        amqpAdmin.declareExchange(new DirectExchange("amqpAdmin.exchange"));
        // 创建队列
        amqpAdmin.declareQueue(new Queue("amqpAdmin.queue", true));
        // 创建绑定规则
        amqpAdmin.declareBinding(new Binding("amqpAdmin.queue", Binding.DestinationType.QUEUE, "amqpAdmin.exchange", "amqp.haha", null));
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


    @Test
    void testJestClient() {
        // 1. 给ES中索引(保存)一个文章
        Article article = new Article();
        article.setId(1);
        article.setTitle("好消息2");
        article.setAuthor("lisi");
        article.setContent("Hello World1");

        // 构建一个索引功能
        Index build = new Index.Builder(article).index("atguigu").type("news").build();

        try {
            jestClient.execute(build);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 测试搜索
    @Test
    void testSearch() {
        // 查询表达式
        String json = "{\n" +
                "    \"query\" : {\n" +
                "        \"match\" : {\n" +
                "            \"content\" : \"Hello\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        // 构建一个搜索功能
        Search build = new Search.Builder(json).addIndex("atguigu").addType("news").build();

        // 执行
        try {
            SearchResult execute = jestClient.execute(build);
            System.out.println(execute.getJsonString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBookSearch() {
//        Book book = new Book();
//        book.setId(1);
//        book.setBookName("南京");
//        book.setAuthor("吕高忍");
//        bookRepository.index(book);
        for(Book book : bookRepository.findByBookNameLike("南")){
            System.out.println(book);
        }
    }

}
