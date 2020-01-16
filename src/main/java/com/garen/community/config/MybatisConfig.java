package com.garen.community.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * MyBatis配置类
 * Created by macro on 2019/4/8.
 */
@Configuration
@MapperScan(basePackages = {"com.garen.community.mapper", "com.garen.community.mbg.mapper"})
public class MybatisConfig {
}
