package com.xxxx.seckill.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xxxx.seckill.pojo.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {
    //定义密钥(也可以放在配置springboot配置文件中或服务器环境变量中)
    private static final String SHARK = "abcdefghigklmnopqrstuvwxyz123456";

    //生成token
    public static String generateToken(User user) {
        Date now = new Date();
        int expire = 360000;  //设置过期时间
        Date expiration = new Date(now.getTime() + 1000 * expire);
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(user.getId()));
        map.put("name", user.getNickname());
        map.put("permission", "All");
        ObjectMapper objectMapper = new ObjectMapper();
        String subject = null;
        try {
            subject = objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SHARK)  //采取SHA512加密算法
                .compact();
    }

    //解析token
    public static Claims getClaimsVyToken(String token) {
        return Jwts.parser()
                .setSigningKey(SHARK)
                .parseClaimsJws(token)
                .getBody();
    }
}
