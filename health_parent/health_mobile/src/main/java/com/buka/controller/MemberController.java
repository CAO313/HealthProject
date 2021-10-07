package com.buka.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.buka.constant.MessageConstant;
import com.buka.constant.RedisMessageConstant;
import com.buka.entity.Result;
import com.buka.pojo.Member;
import com.buka.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {
    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/login")
    public Result login(@RequestBody Map map) throws Exception {
        String telephone =(String)map.get("telephone");
        String validateCode =(String) map.get("validateCode");
        String validateCodeInRedis = jedisPool.getResource().get(telephone+ RedisMessageConstant.SENDTYPE_LOGIN);
        if(validateCode!=null&&validateCodeInRedis!=null&&validateCode.equals(validateCodeInRedis)){
                Member member = memberService.findByTelephone(telephone);
                if (member == null){
                    member = new Member();
                    member.setPhoneNumber(telephone);
                    member.setRegTime(new Date());
                    memberService.add(member);
                }
                return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
        else {
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
