package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.mapper.SeckillOrderMapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillGoods;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.UUIDUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huahua
 * @since 2025-02-09
 */
@Service
@Primary
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    ISeckillGoodsService goodsService;
    @Autowired
    IGoodsService service;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    SeckillOrderMapper seckillOrderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    @Transactional
    public Order seckill(String userId, GoodsVo goods) {
            //更新库存
            SeckillGoods seckillGoods = goodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
            goodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count =" +
                "stock_count -1").eq("goods_id", goods.getId()).gt("stock_count",0));
                //生成订单
                Order order = new Order();
                order.setUserId(userId);
                order.setGoodsId(goods.getId());
                order.setDeliveryAddrId(0L);
                order.setGoodsName(goods.getGoodsName());
                order.setGoodsCount(1);
                order.setGoodsPrice(seckillGoods.getSeckillPrice());
                order.setOrderChannel(1);
                order.setStatus(0);
                order.setCreateDate(new Date());
                orderMapper.insert(order);
                System.out.println("how");
                //生成秒杀订单
                SeckillOrder seckillOrder = new SeckillOrder();
                seckillOrder.setGoodsId(goods.getId());
                seckillOrder.setOrderId(order.getId());
                seckillOrder.setUserId(userId);
                int insert = seckillOrderMapper.insert(seckillOrder);//创建了索引，插入不进去
                System.out.println(insert);
                System.out.println("huahua");
                //在redis中生成订单
                redisTemplate.opsForValue().set("order:"+userId+":"+goods.getId(), JsonUtil.object2JsonStr(seckillOrder),50, TimeUnit.MINUTES);
                return order;
    }

    @Override
    public OrderDetailVo detail(Long orderId) {
        if(orderId == null)
        {
            throw new GlobalException(RespBeanEnum.ERROR);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = service.findGoodsVoById(order.getGoodsId());
        OrderDetailVo detailVo = new OrderDetailVo();
        detailVo.setGoodsVo(goodsVo);
        detailVo.setOrder(order);
        return detailVo;
    }

    @Override
    public boolean checkPath(String userId, Long goodsId, String path) {
        if(userId == null || StringUtils.isEmpty(path))
        {
            return false;
        }
        String redisPath = (String)redisTemplate.opsForValue().get("seckillPath:"+userId+":"+goodsId);
        System.out.println("seckillPath:"+userId+":"+goodsId);
        System.out.println("redisPath:"+redisPath);
        System.out.println("path:"+path);
        return path.equals(redisPath);
    }

    @Override
    public String createPath(String userId, Long goodsId) {
        String str = MD5Util.md5(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:"+userId+":"+goodsId,str,60,TimeUnit.SECONDS);
        return str;
    }

    @Override
    public boolean checkCap(String userId, Long goodsId, String cap) {
        if(StringUtils.isEmpty(cap)|| null == userId || goodsId<0)
        {
            return false;
        }
        String o = (String)redisTemplate.opsForValue().get("cap:" + userId + ":" + goodsId);
        return o.equals(cap);
    }
}
