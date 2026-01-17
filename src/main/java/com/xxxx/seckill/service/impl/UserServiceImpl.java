package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.exception.GlobalException;
import com.xxxx.seckill.mapper.UserMapper;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.*;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;
import sun.nio.cs.US_ASCII;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author huahua
 * @since 2025-02-04
 */
@Service
@Primary
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    JwtUtils jwtUtils;
    @Override
    public RespBean doLogin(LoginVo loginVo,HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
//        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password))
//        {
//            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
//        }
//        if(!ValidatorUtil.isMobile(mobile))
//        {
//            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
//        }
        User user = userMapper.selectById(mobile);
        System.out.println("User:"+user);
        if(null == user)
        {
            throw new GlobalException(RespBeanEnum.User_ERROR);
        }
        if(!MD5Util.fromPassToDBPass(password,user.getSalt()).equals(user.getPassword()))
        {
            throw new GlobalException(RespBeanEnum.PASSWORD_ERROR);
        }
        System.out.println("isOK!");
        String jwt = JwtUtils.generateToken(user);
        //String ticket = UUIDUtil.uuid();
        //request.getSession().setAttribute(ticket,user);
        //将用户信息存入到redis中
        //redisTemplate.opsForValue().set("user:"+ticket,user);
        //浏览器会将该 Cookie 发送回服务器，服务器通过验证 Cookie 中的信息来确认用户的身份，从而保持用户的登录状态
//        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(jwt);
    }

    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
        if(StringUtils.isEmpty(userTicket))
        {
            return null;
        }
        //从redis中获取用户
        User user = (User)redisTemplate.opsForValue().get("user:" + userTicket);
        if(user!=null)
        {
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }

    @Override
    public RespBean updatePassword(String userTicket, String password, HttpServletRequest request, HttpServletResponse response) {
        User user = getUserByCookie(userTicket, request, response);
        if(user == null)
        {
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToPass(password));
        int result = userMapper.updateById(user);
        if(1 == result)
        {
            redisTemplate.delete("user:"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }
}
