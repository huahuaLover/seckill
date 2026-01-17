package com.xxxx.seckill.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQDirectConfig {
//    private static final String QUEUE01 = "queue_direct01";
//    private static final String QUEUE02 = "queue_direct02";
//    private static final String EXCHANGE = "directExchange";
//    private static final String ROUTKEY01 = "red";
//    private static final String ROUTKEY02 = "green";
//    @Bean
//    public Queue queue03()
//    {
//        return new Queue(QUEUE01);
//    }
//    @Bean
//    public Queue queue04()
//    {
//        return new Queue(QUEUE02);
//    }
//    @Bean
//    public DirectExchange directExchange()
//    {
//        return new DirectExchange(EXCHANGE);
//    }
//    @Bean
//    public Binding binding03()
//    {
//        return BindingBuilder.bind(queue03()).to(directExchange()).with(ROUTKEY01);
//    }
//    @Bean
//    public Binding binding04()
//    {
//        return BindingBuilder.bind(queue04()).to(directExchange()).with(ROUTKEY02);
//    }
}
