package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author huahua
 * @since 2025-02-09
 */
public interface IOrderService extends IService<Order> {

    Order seckill(String userId, GoodsVo goods);
    OrderDetailVo detail(Long orderId);

    boolean checkPath(String userId, Long goodsId, String path);

    String createPath(String userId, Long goodsId);

    boolean checkCap(String userId, Long goodsId, String cap);
}
