package com.jiayou.pets.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @SuppressWarnings("null")
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 对所有路径生效
        registry.addMapping("/**")
                // 前端地址
                .allowedOrigins("*")
                // 允许所有 HTTP 方法
                .allowedMethods("*")
                // 允许所有请求头
                .allowedHeaders("*")
                // 不允许携带 Cookie
                .allowCredentials(false)
                // 预检请求的缓存时间（秒）
                .maxAge(3600);
    }
}