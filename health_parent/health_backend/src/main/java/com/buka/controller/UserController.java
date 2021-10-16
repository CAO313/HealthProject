package com.buka.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.buka.constant.MessageConstant;
import com.buka.entity.Result;
import com.buka.pojo.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    @RequestMapping("/getUserName")
    public Result getUserName(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null){
        String userName = user.getUsername();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,userName);
        }else {
            return new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }
    }
}
