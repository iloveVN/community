package com.garen.community.config;


import com.garen.community.config.interceptors.LoggerInterceptor;
import com.garen.community.config.interceptors.SessionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CustomMVCConfig implements WebMvcConfigurer {

    /**
     * 添加过滤器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionInterceptor()).addPathPatterns("/**").excludePathPatterns("/druid/*");
        registry.addInterceptor(new LoggerInterceptor()).addPathPatterns("/**");
    }
}
