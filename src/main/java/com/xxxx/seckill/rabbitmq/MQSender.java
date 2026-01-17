package com.xxxx.seckill.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
@Slf4j
public class MQSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;
//    public void send(Object msg)
////    {
////        log.info("发送消息"+msg);
//////        rabbitTemplate.convertAndSend("queue",msg);
////        rabbitTemplate.convertAndSend("fanoutExchange","",msg);
////    }
////    public void send01(Object msg)
////    {
////        log.info("发送red消息:"+msg);
////        rabbitTemplate.convertAndSend("directExchange","red",msg);
////    }
////    public void send02(Object msg)
////    {
////        log.info("发送green消息:"+msg);
////        rabbitTemplate.convertAndSend("directExchange","green",msg);
////    }
////    public void send03(Object msg)
////    {
////        log.info("发送01接收:"+msg);
////        rabbitTemplate.convertAndSend("topicExchange","queue.red.message",msg);
////    }
////    public void send04(Object msg)
////    {
////        log.info("发送02接收:"+msg);
////        rabbitTemplate.convertAndSend("topicExchange","message.queue.green.abc",msg);
////    }
////    public void send05(String msg)
////    {
////        log.info("发送消息被两个接收:"+msg);
////        MessageProperties properties = new MessageProperties();
////        properties.setHeader("color","red");
////        properties.setHeader("speed","fast");
////        Message message = new Message(msg.getBytes(),properties);
////
////        rabbitTemplate.convertAndSend("headerExchange","",message);
////    }
////    public void send06(String msg)
////    {
////        MessageProperties properties = new MessageProperties();
////        properties.setHeader("color","red");
////        Message message = new Message(msg.getBytes(),properties);
////
////        log.info("发送01接收:"+msg);
////        rabbitTemplate.convertAndSend("headerExchange","",message );
////    }
    public void sendseckillMessage(String message)
    {
        log.info("发送消息:"+message);
        rabbitTemplate.convertAndSend("seckillExchange","seckill.msg",message);
    }
}
