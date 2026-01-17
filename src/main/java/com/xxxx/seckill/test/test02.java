package com.xxxx.seckill.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class test02 {
    @Autowired
    private RedisTemplate redisTemplate;
    public void testString()
    {
        redisTemplate.opsForValue().set("name","lht");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }
    public static void main(String[] args) {
        test02 t = new test02();
        t.testString();
    }
}
