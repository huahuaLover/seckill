package com.xxxx.seckill.controller;

import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.vo.DetailVo;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.jws.WebParam;
import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
如果用户账号和密码都是正确的，会生成一个cookie，发送给浏览器，并且会将用户信息存入到redis中
浏览器会将cookie发送到/goods/toList用来验证用户的合法性
通过cookie在redis中进行查找
 */
//吞吐量1359
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IUserService service;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    ThymeleafViewResolver thymeleafViewResolver;
    @Autowired
    private IGoodsService goodsService;
    @RequestMapping(value = "/toList",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, User user,HttpServletRequest request,
                         HttpServletResponse response)
    {
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        String html = (String)valueOperations.get("goodsList");
//        if(!StringUtils.isEmpty(html))
//        {
//            return html;
//        }

//        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodsVo());
        WebContext webContext = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        String html = thymeleafViewResolver.getTemplateEngine().process("goodsList",webContext);
//        if(!StringUtils.isEmpty(html))
//        {
//            valueOperations.set("goodsList",html, 60,TimeUnit.SECONDS);
//        }
        return html;
    }
    @RequestMapping(value = "/toDetail2/{goodsId}",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toDetail2(Model model, User user, @PathVariable Long goodsId,HttpServletRequest request,
                           HttpServletResponse response)
    {
         model.addAttribute("user",user);
         GoodsVo vo = goodsService.findGoodsVoById(goodsId);
         Date startDate = vo.getStartDate();
         Date endDate = vo.getEndDate();
         Date nowDate = new Date();
         int seckillStatus = 0;
         int remainSeconds = 0;
         if(nowDate.before(startDate))
         {
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime())/1000);
             System.out.println("remain"+remainSeconds);
         }
         else if(nowDate.after(endDate))
         {
             seckillStatus = 2;
             remainSeconds = -1;
             System.out.println("结束");
         }
         else
         {
             seckillStatus = 1;
             remainSeconds = 0;
             System.out.println("进行中");
         }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String)valueOperations.get("goodsDetail" + goodsId);
        if(!StringUtils.isEmpty(html))
        {
            return html;
        }
         model.addAttribute("remainSeconds",remainSeconds);
         model.addAttribute("secKillStatus",seckillStatus);
         model.addAttribute("goods",vo);
        WebContext webContext = new WebContext(request, response, request.getServletContext(),
                request.getLocale(), model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsDetail",webContext);
        if(!StringUtils.isEmpty(html))
        {
            valueOperations.set("goodsDetail"+goodsId,html, 60,TimeUnit.SECONDS);
        }
        return html;
    }

    @RequestMapping(value = "/toDetail/{goodsId}")
    @ResponseBody
    //后端只是返回了一个对象
    public RespBean toDetail(Model model, User user, @PathVariable Long goodsId, HttpServletRequest request,
                             HttpServletResponse response)
    {
        System.out.println("goodsId"+goodsId);
        GoodsVo vo = goodsService.findGoodsVoById(goodsId);
        Date startDate = vo.getStartDate();
        Date endDate = vo.getEndDate();
        Date nowDate = new Date();
        int seckillStatus = 0;
        int remainSeconds = 0;
        if(nowDate.before(startDate))
        {
            remainSeconds = (int)((startDate.getTime() - nowDate.getTime())/1000);
            System.out.println("remain"+remainSeconds);
        }
        else if(nowDate.after(endDate))
        {
            seckillStatus = 2;
            remainSeconds = -1;
            System.out.println("结束");
        }
        else
        {
            seckillStatus = 1;
            remainSeconds = 0;
            System.out.println("进行中");
        }
        DetailVo detailVo = new DetailVo();
        detailVo.setGoodsVo(vo);
        detailVo.setUser(user);
        detailVo.setSeckillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        System.out.println("xxxxxxxxxxxxxxxxxxx");
        return RespBean.success(detailVo);

    }
}
