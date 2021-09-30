package com.buka.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.buka.constant.MessageConstant;
import com.buka.entity.Result;
import com.buka.pojo.TSetmeal;
import com.buka.service.SetMealService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    @RequestMapping("/getAllSetmeal")
    public Result getAllSetmeal(){
        try{
            List<TSetmeal> list = setMealService.getAll();
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }
    @RequestMapping("/findById")
    public Result findById(@Param("id") Integer id){
        try{
            TSetmeal tSetmeal = setMealService.findByID(id);
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,tSetmeal);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }
}
