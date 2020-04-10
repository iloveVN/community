package com.garen.community.config;

import com.garen.community.config.filter.MyFilter;
import com.garen.community.config.listener.MyListener;
import com.garen.community.config.servlet.MyServlet;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.Servlet;
import java.util.Arrays;

@Configuration
public class MyServerConfig {

    // 定制嵌入式的Servlet容器的相关规则
    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> webServerFactoryCustomizer() {

        return new WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>(){
            @Override
            public void customize(ConfigurableServletWebServerFactory factory) {
                factory.setPort(8089);
            }
        };
    }

    /**
     * 由于springboot默认是以jar包的方式启动嵌入式的Servlet容器来启动SpringBoot的web应用。没有web.xml文件，
     * 所以需要使用一下的方式注册三大组件
     * 1. ServletRegistrationBean   注册servlet
     * 2. FilterRegistrationBean    注册过滤器
     * 3. ServletListenerRegistrationBean 注册监听器
     *
     * SpringBoot帮我们自动SpringMVC的时候，自动注册SpringMVC的前端控制器，DispatcherServlet --》具体可看DispatcherServletAutoConfiguration中查看
     *
     * @return
     */
    // 注册servlet
    @Bean
    public ServletRegistrationBean myServlet() {
        ServletRegistrationBean<MyServlet> myServletServletRegistrationBean =
                    new ServletRegistrationBean<>(new MyServlet(), "/myServlet");
        myServletServletRegistrationBean.setLoadOnStartup(1);  // 启动顺序
        return myServletServletRegistrationBean;
    }

    // 注册过滤器
    @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new MyFilter());
        filterFilterRegistrationBean.setUrlPatterns(Arrays.asList("/hello", "/myServlet"));
        return filterFilterRegistrationBean;
    }

    // 注册监听器
    @Bean
    public ServletListenerRegistrationBean myListener() {
        ServletListenerRegistrationBean servletListenerRegistrationBean = new ServletListenerRegistrationBean(new MyListener());
        return servletListenerRegistrationBean;
    }
}
