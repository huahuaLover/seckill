package com.xxxx.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.xxxx.seckill.*")
@EnableAspectJAutoProxy(exposeProxy = true) // 关键配置
public class SeckillDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeckillDemoApplication.class, args);
    }
}
