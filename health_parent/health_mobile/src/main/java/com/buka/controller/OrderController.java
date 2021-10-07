package com.buka.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.buka.constant.MessageConstant;
import com.buka.constant.RedisMessageConstant;
import com.buka.entity.Result;
import com.buka.pojo.Order;
import com.buka.service.OrderService;
import com.buka.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        Result result = null;
        String telephone = (String)map.get("telephone");
        String validateCode =(String)map.get("validateCode");
        String validateCodeInRedis = jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_ORDER);
        if(validateCode!=null && validateCodeInRedis!=null && validateCodeInRedis.equals(validateCode)){
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            try{
                result = orderService.order(map);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }
            if(result.isFlag()){
                try{
                    SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,"预约成功");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        else{
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
        return result;
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
