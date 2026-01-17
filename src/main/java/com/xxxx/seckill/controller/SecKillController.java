package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import com.xxxx.seckill.config.AccessLimit;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.mapper.SeckillOrderMapper;
import com.xxxx.seckill.pojo.*;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import com.xxxx.seckill.vo.SeckillMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.map.repository.config.EnableMapRepositories;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.Transient;
import java.io.IOException;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seckill")
@Slf4j
public class SecKillController implements InitializingBean {
    @Autowired
    IGoodsService goodsService;
    @Autowired
    ISeckillGoodsService ISeckillGoodsService;
    @Autowired
    ISeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Qualifier("stock")
    @Autowired
    private DefaultRedisScript script;
    @Qualifier("lockSet")
    @Autowired
    private DefaultRedisScript lockScript;
    @Autowired
    private MQSender sender;
    @Autowired
    IOrderService orderService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    SeckillOrderMapper seckillOrderMapper;
    //活动时间30分钟
    private static final int TTL_TIME = 30*60;
    private Map<Long,Boolean> EmptyStockMap = new HashMap<>();
    @PostMapping("/doSeckillOnePeopleOneOrder")
    @ResponseBody
    public RespBean doSeckillNoPathAndOnePeopleOneOrder(Long goodsId, HttpServletRequest request)
    {
        Subject subject = (Subject)request.getAttribute("user");
        if(subject == null)
        {
            return RespBean.error(RespBeanEnum.User_ERROR);
        }
        //synchronized (subject.getId().intern())
        //{
            SecKillController proxy = (SecKillController)AopContext.currentProxy();
            //获得当前代理
            //return proxy.createOrderSync(goodsId,subject);
            return proxy.createOrderDistributedLockWithSet(goodsId,subject);
       // }
    }
    @Transactional
    public RespBean createOrderDistributedLockWithSet(Long goodsId,Subject subject)
    {
        //内存标记,减少redis的访问
        if(EmptyStockMap.get(goodsId))
        {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }


        Long lockResult =(Long) redisTemplate.execute(lockScript, Arrays.asList("goodsId:"+goodsId),subject.getId());
        if(lockResult==1)
        {
            return RespBean.error(RespBeanEnum.Lock_ERROR);
        }
        //判断是否重复抢购
        Object seckillOrderObject = redisTemplate.opsForValue().get("order:"+subject.getId()+":"+goodsId);
        if(seckillOrderObject!=null)
        {
            //释放锁
            redisTemplate.opsForSet().remove("goodsId:"+goodsId,subject.getId());
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        String orderKey = "order:"+subject.getId()+":"+goodsId;
        String storeKey = "seckillGoods:"+goodsId;
        //通过 Redis 执行一段 Lua 脚本，并返回一个 Long 类型的结果，进行原子性的减库存操作
        //Long result =(Long) redisTemplate.execute(script, Arrays.asList(storeKey,orderKey),Collections.EMPTY_LIST);
        Long result =(Long) redisTemplate.execute(script, Arrays.asList(storeKey),Collections.EMPTY_LIST);
        if(result==-1)
        {
            EmptyStockMap.put(goodsId,true);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        else if(result == -2)
        {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        log.info("ready to send rabbit mq message");
        SeckillMessage seckillMessage = new SeckillMessage(subject.getId(),goodsId);
        sender.sendseckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
    }
    @Transactional
    public RespBean createOrderDistributedLock(Long goodsId,Subject subject)
    {
        //内存标记,减少redis的访问
        if(EmptyStockMap.get(goodsId))
        {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        //判断是否重复抢购
        Object seckillOrderObject = redisTemplate.opsForValue().get("order:"+subject.getId()+":"+goodsId);
        if(seckillOrderObject!=null)
        {

            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        boolean lock = redisTemplate.opsForValue().setIfAbsent("order:"+goodsId+":"+subject.getId(),subject.getId().toString(),6000,TimeUnit.SECONDS);
        if(!lock)
        {
            return RespBean.error(RespBeanEnum.Lock_ERROR);
        }

        String orderKey = "order:"+subject.getId()+":"+goodsId;
        String storeKey = "seckillGoods:"+goodsId;
        //通过 Redis 执行一段 Lua 脚本，并返回一个 Long 类型的结果，进行原子性的减库存操作
        //Long result =(Long) redisTemplate.execute(script, Arrays.asList(storeKey,orderKey),Collections.EMPTY_LIST);
        Long result =(Long) redisTemplate.execute(script, Arrays.asList(storeKey),Collections.EMPTY_LIST);
        if(result==-1)
        {
            EmptyStockMap.put(goodsId,true);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        else if(result == -2)
        {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        log.info("ready to send rabbit mq message");
        SeckillMessage seckillMessage = new SeckillMessage(subject.getId(),goodsId);
        sender.sendseckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
    }
    @Transactional
    public RespBean createOrderSync(Long goodsId,Subject subject)
    {
        //内存标记,减少redis的访问
        if(EmptyStockMap.get(goodsId))
        {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
            String orderKey = "order:"+subject.getId()+":"+goodsId;
            String storeKey = "seckillGoods:"+goodsId;
            //判断是否重复抢购
            Object seckillOrderObject = redisTemplate.opsForValue().get("order:"+subject.getId()+":"+goodsId);
            if(seckillOrderObject!=null)
            {
                return RespBean.error(RespBeanEnum.REPEAT_ERROR);
            }
            //通过 Redis 执行一段 Lua 脚本，并返回一个 Long 类型的结果，进行原子性的减库存操作
            //Long result =(Long) redisTemplate.execute(script, Arrays.asList(storeKey,orderKey),Collections.EMPTY_LIST);
            Long result =(Long) redisTemplate.execute(script, Arrays.asList(storeKey),Collections.EMPTY_LIST);
            if(result==-1)
            {
                EmptyStockMap.put(goodsId,true);
                return RespBean.error(RespBeanEnum.EMPTY_STOCK);
            }
            else if(result == -2)
            {
                return RespBean.error(RespBeanEnum.REPEAT_ERROR);
            }
            log.info("ready to send rabbit mq message");
        //更新库存
        SeckillGoods seckillGoods = ISeckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id", goodsId));
        SeckillGoods good = ISeckillGoodsService.getOne(new QueryWrapper<SeckillGoods>().eq("goods_id",goodsId));
        //乐观锁
        int version = good.getVersion();
        if(good.getStockCount()<=0)
        {
            return null;
        }
        GoodsVo goods = goodsService.findGoodsVoById(goodsId);
        boolean result2 = ISeckillGoodsService.update(new UpdateWrapper<SeckillGoods>().setSql("stock_count =" +
                "stock_count -1").eq("goods_id", goods.getId()).gt("stock_count",0));
        if(!result2)
        {
            log.info("扣除失败");
            return null;
        }
        else
        {
            //生成订单
            Order order = new Order();
            order.setUserId(subject.getId());
            order.setGoodsId(goodsId);
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
            seckillOrder.setUserId(subject.getId());
            int insert = seckillOrderMapper.insert(seckillOrder);//创建了索引，插入不进去
            System.out.println(insert);
            System.out.println("huahua");
            redisTemplate.opsForValue().set("order:"+subject.getId()+":"+goods.getId(), JsonUtil.object2JsonStr(seckillOrder),50, TimeUnit.MINUTES);
        }
            return RespBean.success(0);
    }
    @PostMapping("/doSeckill")
    @ResponseBody
    @Transient
    public RespBean doSeckillNoPath(Long goodsId, HttpServletRequest request)
    {
        /*
        减少数据库访问
        1.系统初始化,把商品库存数量加载到Redis中
        2.收到请求,Redis预减库存.库存不足,直接返回.否则直接进入第三步
        3.请求入队,立即返回排队中
        4.请求出队,生成订单,减少库存
        5.客户端轮询,是否秒杀成功
         */
        Subject subject = (Subject)request.getAttribute("user");
        if(subject == null)
        {
            return RespBean.error(RespBeanEnum.User_ERROR);
        }
        //内存标记,减少redis的访问
        if(EmptyStockMap.get(goodsId))
        {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        String storeKey = "seckillGoods:"+goodsId;
        String orderKey = "order:"+goodsId;
        //key是商品维度的，value是用户id，存入的是Set
        Long result =(Long) redisTemplate.execute(script, Arrays.asList(storeKey,orderKey),subject.getId(),TTL_TIME);
        if(result == -1)
        {
            EmptyStockMap.put(goodsId,true);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        else if(result == -2)
        {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        log.info("ready to send rabbit mq message");
        SeckillMessage seckillMessage = new SeckillMessage(subject.getId(),goodsId);
        sender.sendseckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
    }
    @PostMapping("/{path}/doSeckill")
    @ResponseBody
    @Transient
    public RespBean doSeckill(@PathVariable String path, Long goodsId, HttpServletRequest request)
    {
        /*
        减少数据库访问
        1.系统初始化,把商品库存数量加载到Redis中
        2.收到请求,Redis预减库存.库存不足,直接返回.否则直接进入第三步
        3.请求入队,立即返回排队中
        4.请求出队,生成订单,减少库存
        5.客户端轮询,是否秒杀成功
         */
        Subject subject = (Subject)request.getAttribute("user");
        if(subject == null)
        {
            return RespBean.error(RespBeanEnum.User_ERROR);
        }

        System.out.println("doseckill"+goodsId);
        //判断接口
        boolean check = orderService.checkPath(subject.getId(),goodsId,path);
        if(!check)
        {
            return RespBean.error(RespBeanEnum.PATH_ERROR);
        }
        //判断是否重复抢购
        Object seckillOrderObject = redisTemplate.opsForValue().get("order:"+subject.getId()+":"+goodsId);
        if(seckillOrderObject!=null)
        {
            return RespBean.error(RespBeanEnum.REPEAT_ERROR);
        }
        //内存标记,减少redis的访问
        if(EmptyStockMap.get(goodsId))
        {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //通过 Redis 执行一段 Lua 脚本，并返回一个 Long 类型的结果，进行原子性的减库存操作
        Long stock =(Long) redisTemplate.execute(script, Collections.singletonList("seckillGoods:"+goodsId),Collections.EMPTY_LIST);
        if(stock<0)
        {
            EmptyStockMap.put(goodsId,true);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        SeckillMessage seckillMessage = new SeckillMessage(subject.getId(),goodsId);
        sender.sendseckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success(0);
    }

    @Override
    @Transactional
    public void afterPropertiesSet() throws Exception {
        List<GoodsVo> list = goodsService.findGoodsVo();
        if(CollectionUtils.isEmpty(list))
        {
            return;
        }
        list.forEach(goodsVo -> {
            redisTemplate.opsForValue().set("seckillGoods:"+goodsVo.getId(),goodsVo.getStockCount());
            //第一次进行创建
            EmptyStockMap.put(goodsVo.getId(),false);
        });
    }
    @RequestMapping(value = "/result",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getResult(User user,Long goodsId)
    {
        if(user == null)
        {
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }
        Long orderId = seckillOrderService.getResult(user,goodsId);
        return RespBean.success(orderId);
    }
    @RequestMapping(value = "/path",method = RequestMethod.GET)
    @ResponseBody
    @AccessLimit(second = 5, maxCount = 5, needLogin = true)
    public RespBean getPath(Long goodsId,String cap,HttpServletRequest request)
    {
        Subject subject = (Subject) request.getAttribute("user");
        if(subject==null)
        {
            return RespBean.error(RespBeanEnum.ERROR);
        }
        String url = request.getRequestURI();
        ValueOperations valueOperations = redisTemplate.opsForValue();


//        Integer count = (Integer) valueOperations.get(url+":"+user.getId());
//        if(count == null)
//        {
//            valueOperations.set(url+":"+user.getId(),1,5, TimeUnit.SECONDS);
//        }
//        else if (count<5)
//        {
//            valueOperations.increment(url+":"+user.getId());
//        }
//        else {
//            return RespBean.error(RespBeanEnum.ACCESS_LIMIT_REACHED);
//        }
        boolean check = orderService.checkCap(subject.getId(),goodsId,cap);
        if(!check)
        {
            return RespBean.error(RespBeanEnum.REQUEST_CAP);
        }

        String str = orderService.createPath(subject.getId(),goodsId);
        log.info("path:"+str);
        return RespBean.success(str);
    }
    @RequestMapping(value = "/cap",method = RequestMethod.GET)
    public void verifyCode(Long goodsId, HttpServletRequest request,HttpServletResponse response)  {
        Subject subject = (Subject) request.getAttribute("user");
        log.info("subject:{}",subject);
        if(subject.getId()==null||goodsId<0)
        {
            throw new GlobalException(RespBeanEnum.REQUEST_ILLEGAL);
        }
        response.setContentType("image/jpg");
        response.setHeader("Pragma","No-cache");
        response.setHeader("Cache-Control","no-cache");
        response.setDateHeader("Expires",0);
        //生成验证码，输入redis中
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130,32,3);
        redisTemplate.opsForValue().set("cap:"+subject.getId()+":"+goodsId,captcha.text(),300, TimeUnit.SECONDS);
        try {
            captcha.out(response.getOutputStream());
        }
        catch (IOException e)
        {
           log.error("验证码生成失败",e.getMessage());
        }


    }
}
