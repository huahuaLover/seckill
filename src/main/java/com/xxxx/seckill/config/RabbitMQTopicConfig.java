package com.xxxx.seckill.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQTopicConfig {
//    其中*匹配一个字符
    //#匹配多个字符,其中`.`进行分割
//    private static final String QUEUE01 = "queue_topic01";
//    private static final String QUEUE02 = "queue_topic02";
//    private static final String EXCHANGE = "topicExchange";
//    private static final String ROUTKEY01 = "#.queue.#";
//    private static final String ROUTKEY02 = "*.queue.#";
//    @Bean
//    public Queue queue05()
//    {
//        return new Queue(QUEUE01);
//    }
//    @Bean
//    public Queue queue06()
//    {
//        return new Queue(QUEUE02);
//    }
//    @Bean
//    public TopicExchange topicExchange()
//    {
//        return new TopicExchange(EXCHANGE);
//    }
//    @Bean
//    public Binding binding05()
//    {
//        return BindingBuilder.bind(queue05()).to(topicExchange()).with(ROUTKEY01);
//    }
//    @Bean
//    public Binding binding06()
//    {
//        return BindingBuilder.bind(queue06()).to(topicExchange()).with(ROUTKEY02);
//    }
    private static final String QUEUE = "seckillQueue";
    private static final String EXCHANGE = "seckillExchange";
    @Bean
    public Queue queue()
    {
        return new Queue(QUEUE);
    }
    @Bean
    public TopicExchange topicExchange()
    {
        return new TopicExchange(EXCHANGE);
    }
    @Bean
    public Binding binding01()
    {
        return BindingBuilder.bind(queue()).to(topicExchange()).with("seckill.#");
    }
}
