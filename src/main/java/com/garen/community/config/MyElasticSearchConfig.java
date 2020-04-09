package com.garen.community.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * SpringBoot默认支持两种技术来和ES交互
 * 1. Jest (默认不生效)
 *  需要导入jest工具包
 * 2. SpringData ElasticSearch 【ES版本有可能不适配】 一定要保证版本适配，不然会出现各种问题。
 *      版本适配说明: https://github.com/spring-projects/spring-data-elasticsearch
 *      如果版本不适配
 *          1. 升级springboot版本
 *          2. 安装对应的ES版本
 *      1)、Client客户端: 需要配置clusterNodes(集群节点信息)，clusterName
 *      2)、ElasticSearchTemplate 操作es
 *      3)、编写一个ElasticSearchRepository的子接口来操作ES
 *   两种用法
 *      1)、编写一个ElasticSearchRepository的子接口来操作ES
 *
 *   重点: 在同时整合Actuator场景时，需要注意，此时Actuator会自动依赖ElasticSearch的REST Client，因此同时Rest默认连接的是本机的elasticSearch即uris: http://localhost:9200
 *          因此我们需要指定真实的elasticsearch的uris: 本项目使用的是spring.elasticsearch.rest.uris=http://47.105.154.233:9200
 *
 */
@Configuration
public class MyElasticSearchConfig {
    /**
     * Springboot整合Elasticsearch 在项目启动前设置一下的属性，防止报错
     * 解决netty冲突后初始化client时还会抛出异常
     * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
     */
    @PostConstruct
    void init() {
        System.setProperty("es.set.netty.runtime.available.processors", "false");
    }

}
