package com.xxxx.seckill.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.seckill.pojo.Subject;
import com.xxxx.seckill.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {//让服务器正确响应，浏览器的预请求
            return true;
        }
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) { // 匹配 Cookie 名称
                    log.info("token:"+cookie.getName());
                    log.info("tokenValue:"+cookie.getValue());
                    token = cookie.getValue(); // 获取 token 值
                    break;
                }
            }
        }
        if(token == null){
            System.out.println("token is null");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString("令牌无效，403！");
            response.getWriter().write(jsonResponse);
            return false;
        }
//        String token = request.getHeader("Authorization");
//        if (token == null || !token.startsWith("Bearer ")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsonResponse = objectMapper.writeValueAsString("令牌无效，403！");
//            response.getWriter().write(jsonResponse);
//            return false;
//        }
//        token = token.replace("Bearer ", "");
        try {
            Claims claims = JwtUtils.getClaimsVyToken(token);
            // 反序列化为 User 对象
            ObjectMapper objectMapper = new ObjectMapper();
            Subject subject = objectMapper.readValue(claims.getSubject(), Subject.class);

            request.setAttribute("user", subject);
            //log.info("Token subject:{}",subject);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
