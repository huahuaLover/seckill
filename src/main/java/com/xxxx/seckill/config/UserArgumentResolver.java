package com.xxxx.seckill.config;

import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Component
//public class UserArgumentResolver implements HandlerMethodArgumentResolver {
//    @Autowired
//    IUserService service;
//    @Override
//    public boolean supportsParameter(MethodParameter methodParameter) {
//        Class<?> clazz = methodParameter.getParameterType();
//        return clazz== User.class;
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
////        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
////        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
////        String ticket = CookieUtil.getCookieValue(request, "userTicket");
////        if(StringUtils.isEmpty(ticket))
////        {
////            return null;
////        }
////        return service.getUserByCookie(ticket,request,response);
//        //将用户放到线程中，直接在线程中获得
//        return UserContext.getUser();
//    }
//}
