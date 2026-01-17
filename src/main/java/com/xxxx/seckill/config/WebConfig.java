package com.xxxx.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    JwtInterceptor jwtInterceptor;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login/doLogin")
//                .excludePathPatterns("/goods/toList")
                .excludePathPatterns("/**/*.css")   // 排除所有.css文件
                .excludePathPatterns("/**/*.js")    // 排除所有.js文件
                .excludePathPatterns("/**/*.png")   // 排除所有.png图片
                .excludePathPatterns("/**/*.jpg")   // 排除所有.jpg图片
                .excludePathPatterns("/**/*.gif")   // 排除所有.jpg图片
                .excludePathPatterns("/**/*.ico")   // 排除所有.jpg图片
                .excludePathPatterns("/**/*.html") // 排除所有.html文件
                .excludePathPatterns("/login/toLogin");
    }
}
