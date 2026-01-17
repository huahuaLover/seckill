package com.xxxx.seckill.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/demo")
public class DemoController {
    @Autowired
    RedisTemplate redisTemplate;
    @RequestMapping("/hello")
    public String hello(Model model)
    {
        redisTemplate.opsForValue().set("name","lht");
        System.out.println(redisTemplate.opsForValue().get("name"));
        model.addAttribute("name","huahua");
        return "hello";
    }
}
