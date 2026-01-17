package com.xxxx.seckill.rabbitmq;

import com.rabbitmq.tools.json.JSONUtil;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.SeckillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

@Service
@Slf4j
public class MQReceiver {
//    监听queue的队列
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg)
//    {
//        log.info("接收消息:"+msg);
//    }
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg)
//    {
//        log.info("QUEUE01接收消息:"+msg);
//    }
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg)
//    {
//        log.info("QUEUE02接收消息:"+msg);
//    }
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg)
//    {
//        log.info("DirectQUEUE01接收消息:"+msg);
//    }
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg)
//    {
//        log.info("DirectQUEUE02接收消息:"+msg);
//    }
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg)
//    {
//        log.info("TopicQUEUE01接收消息:"+msg);
//    }
//    @RabbitListener(queues = "queue_topic02")
//    public void receive06(Object msg)
//    {
//        log.info("TopicQUEUE02接收消息:"+msg);
//    }
//    @RabbitListener(queues = "queue_header01")
//    public void receive07(Object msg)
//    {
//        log.info("queue_header01接收消息:"+msg);
//    }
//    @RabbitListener(queues = "queue_header02")
//    public void receive08(Object msg)
//    {
//        log.info("queue_header02接收消息:"+msg);
//    }
    @Autowired
    IGoodsService goodsService;
    @Autowired
    IOrderService orderService;
    @Autowired
    RedisTemplate redisTemplate;
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message)
    {
        log.info("QUEUE接收消息"+message);
        //首先先判断库存和是否重复购买
        SeckillMessage msg = JsonUtil.jsonStr2Object(message,SeckillMessage.class);
        Long goodsId = msg.getGoodsId();
        String userId = msg.getUserId();
        GoodsVo goods = goodsService.findGoodsVoById(goodsId);
        orderService.seckill(userId,goods);
    }

}
