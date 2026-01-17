package com.xxxx.seckill.test;

@org.aspectj.lang.annotation.Aspect
public class Aspect {
    public void logBeforeMethod() {
        System.out.println("方法开始执行了..."); // Advice中的具体代码
    }
}
