package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.mapper.SeckillOrderMapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.ISeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {
    @Autowired
    OrderMapper OrderMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Long getResult(User user, Long goodsId) {
        Order Order = OrderMapper.selectOne(new QueryWrapper<Order>().eq("user_Id",user.getId()).eq("goods_id",goodsId));
        if(null != Order)
        {
            return Order.getId();
        }
        else
        {
            if(redisTemplate.hasKey("isStockEmpty:"+goodsId))//有最后实际运行的seckill()函数决定的
            {
                return -1L;
            }
            else
            {
                return 0L;
            }
        }
    }
}
