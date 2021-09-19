package com.buka.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.buka.constant.MessageConstant;
import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.entity.Result;
import com.buka.pojo.CheckItem;
import com.buka.service.CheckItemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkitem")
public class CheckItemController {
    @Reference
    CheckItemService checkItemService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/pagequery")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       return checkItemService.findPage(queryPageBean);
    }

    @RequestMapping("/deleteById")
    public Result deleteById(@Param("id") Integer id){
        try {
            checkItemService.deleteByid(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try{
                checkItemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/findById")
    public Result findById(@Param("id") Integer id){
        try{
        return new Result(true,"获取该用户成功",checkItemService.findById(id));
        }
        catch (Exception e){
            return new Result(false,"获取该用户失败");
        }
    }
}
