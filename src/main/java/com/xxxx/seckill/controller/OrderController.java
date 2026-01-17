package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author huahua
 * @since 2025-02-09
 */
@Controller
@RequestMapping("/order")
public class OrderController implements BeanNameAware {
    @Autowired
    IOrderService orderService;
    private String beanName;
    @RequestMapping("/toDetail")
    @ResponseBody
    public RespBean detail(User user,Long orderId)
    {
        System.out.println(user);
        System.out.println(orderId);
        if(user == null)
        {
            System.out.println("用户不存在");
            return RespBean.error(RespBeanEnum.ERROR);
        }
        OrderDetailVo detailVo = orderService.detail(orderId);
        return RespBean.success(detailVo);
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }
}
