package com.xxxx.seckill.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务器异常xxx"),
    MOBILE_ERROR(500210,"手机号码格式不正确"),
    LOGIN_ERROR(500211,"用户名或密码不能为空"),
    Bind_ERROR(500214,"参数校验异常"),
    PASSWORD_ERROR(500213,"密码不正确"),
    ACCESS_LIMIT_REACHED(500,"访问次数过多，稍后重试"),
    EMPTY_STOCK(500500,"库存不足"),
    REQUEST_ILLEGAL(500500,"请求不合法"),
    REQUEST_CAP(500500,"验证码不正确"),
    PATH_ERROR(500500,"请求路径不正确"),
    MOBILE_NOT_EXIST(500500,"redis中用户不存在"),
    PASSWORD_UPDATE_FAIL(500500,"密码更新错误"),
    REPEAT_ERROR(500501,"每人限购一件"),
    Lock_ERROR(500214,"您已经提交了一个订单，请稍等..."),
    User_ERROR(500212,"用户为空");

    private final Integer code;
    private final String message;

}
