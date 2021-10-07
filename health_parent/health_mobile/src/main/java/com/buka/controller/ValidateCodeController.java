package com.buka.controller;

import com.buka.constant.MessageConstant;
import com.buka.constant.RedisConstant;
import com.buka.constant.RedisMessageConstant;
import com.buka.entity.Result;
import com.buka.utils.SMSUtils;
import com.buka.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/sendCode")
    public Result sendCode(String telephone){
        try{
            Integer integer = ValidateCodeUtils.generateValidateCode(4);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,integer.toString());
            jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,integer.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        try{
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
            jedisPool.getResource().setex(telephone+RedisMessageConstant.SENDTYPE_LOGIN,60*30,code.toString());
            return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.SEND_VALIDATECODE_FAIL);
        }
    }
}
