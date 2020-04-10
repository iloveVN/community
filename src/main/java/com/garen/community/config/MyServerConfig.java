package com.garen.community.config;

import com.garen.community.config.servlet.MyServlet;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;

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


    @Bean
    public ServletRegistrationBean myServlet() {
        ServletRegistrationBean<MyServlet> myServletServletRegistrationBean = new ServletRegistrationBean<>(new MyServlet(), "/myServlet");
        return myServletServletRegistrationBean;
    }


}
