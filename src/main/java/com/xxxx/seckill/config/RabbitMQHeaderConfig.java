package com.xxxx.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQHeaderConfig {
//    private static final String QUEUE01 = "queue_header01";
//    private static final String QUEUE02 = "queue_header02";
//    private static final String EXCHANGE = "headerExchange";
//    @Bean
//    public Queue queue07()
//    {
//        return new Queue(QUEUE01);
//    }
//    @Bean
//    public Queue queue08()
//    {
//        return new Queue(QUEUE02);
//    }
//    @Bean
//    public HeadersExchange headersExchange()
//    {
//        return new HeadersExchange(EXCHANGE);
//    }
//    @Bean
//    public Binding binding07()
//    {
//        Map<String,Object> map = new HashMap<>();
//        map.put("color","red");
//        map.put("speed","low");
//        return BindingBuilder.bind(queue07()).to(headersExchange()).whereAny(map).match();
//    }
//    @Bean
//    public Binding binding08()
//    {
//        Map<String,Object> map = new HashMap<>();
//        map.put("color","red");
//        map.put("speed","fast");
//        return BindingBuilder.bind(queue08()).to(headersExchange()).whereAll(map).match();
//    }
}
