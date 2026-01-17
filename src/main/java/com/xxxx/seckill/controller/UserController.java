package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author huahua
 * @since 2025-02-04
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    MQSender mqSender;
    @RequestMapping("/info")
    @ResponseBody
    public RespBean info(User user)
    {
        return RespBean.success(user);
    }
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq()
//    {
//        mqSender.send("Hello");
//    }
//    @RequestMapping("/fanout")
//    @ResponseBody
//    public void mq2()
//    {
//        mqSender.send("Hello");
//    }
//    @RequestMapping("/direct01")
//    @ResponseBody
//    public void mq01()
//    {
//        mqSender.send01("HELLO,Red");
//    }
//    @RequestMapping("/direct02")
//    @ResponseBody
//    public void mq02()
//    {
//        mqSender.send01("HELLO,Green");
//    }
//    @RequestMapping("/topic01")
//    @ResponseBody
//    public void mq03()
//    {
//        mqSender.send03("HELLO,Red");
//    }
//    @RequestMapping("/topic02")
//    @ResponseBody
//    public void mq04()
//    {
//        mqSender.send04("HELLO,Green");
//    }
//    @RequestMapping("/header01")
//    @ResponseBody
//    public void mq05()
//    {
//        mqSender.send05("HELLO,Red");
//    }
//    @RequestMapping("/header02")
//    @ResponseBody
//    public void mq06()
//    {
//        mqSender.send06("HELLO,Green");
//    }
}
