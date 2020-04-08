package com.garen.community.config;

/**
 * SpringBoot默认支持两种技术来和ES交互
 * 1. Jest (默认不生效)
 *  需要导入jest工具包
 * 2. SpringData ElasticSearch 【ES版本有可能不适配】
 *      版本适配说明: https://github.com/spring-projects/spring-data-elasticsearch
 *      如果版本不适配
 *          1. 升级springboot版本
 *          2. 安装对应的ES版本
 *      1)、Client客户端: 需要配置clusterNodes(集群节点信息)，clusterName
 *      2)、ElasticSearchTemplate 操作es
 *      3)、编写一个ElasticSearchRepository的子接口来操作ES
 *   两种用法
 *      1)、编写一个ElasticSearchRepository的子接口来操作ES
 */
public class MyElasticSearchConfig {
}
