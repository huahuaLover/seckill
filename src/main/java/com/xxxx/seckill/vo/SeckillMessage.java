package com.xxxx.seckill.vo;

import com.xxxx.seckill.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.jws.soap.SOAPBinding;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillMessage {
    private String userId;
    private Long goodsId;
}
