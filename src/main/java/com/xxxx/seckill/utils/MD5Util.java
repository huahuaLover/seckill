package com.xxxx.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

@Component
public class MD5Util {
    public static String md5(String src)
    {
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";

    public static String inputPassToPass(String inputPass)
    {
        return md5("" +salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4));
    }
    public static String fromPassToDBPass(String inputPass,String salt)
    {
        return md5("" +salt.charAt(0)+salt.charAt(2)+inputPass+salt.charAt(5)+salt.charAt(4));
    }
    public static String inputPassToDBPass(String inputPass,String salt)
    {
        String fromPass = inputPassToPass(inputPass);
        String dbPass = fromPassToDBPass(fromPass,salt);
        return dbPass;
    }

    public static void main(String[] args) {
       // System.out.println(inputPassToPass("123456"));
        System.out.println(inputPassToDBPass("18317713768","1a2b3c4d"));
    }
}
