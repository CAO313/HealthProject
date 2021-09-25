package com.buka.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.filter.EchoFilter;
import com.buka.constant.MessageConstant;
import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.entity.Result;
import com.buka.pojo.CheckGroup;
import com.buka.service.CheckGroupService;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.server.ExportException;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {
    @Reference
    CheckGroupService checkGroupService;

    @RequestMapping("/add")
    public Result add(@Param("checkitemIds") Integer[] checkitemIds, @RequestBody CheckGroup checkGroup){
        try{
            checkGroupService.add(checkGroup,checkitemIds);

        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return checkGroupService.findPage(queryPageBean);
    }

    @RequestMapping("/findById")
    public Result findById(@Param("id") Integer id){
        try{
            CheckGroup checkGroup = checkGroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }
    @RequestMapping("/getCheckItemIdsById")
    public Result getCheckItemIdsById(@Param("id") Integer id){
        try{
            Integer[] checkItemIds = checkGroupService.getCheckItemIdsById(id);
            return  new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }
    @RequestMapping("/edit")
    public Result edit(@Param("checkItemIds") Integer[] checkItemIds,@RequestBody CheckGroup checkGroup){
        try{
            checkGroupService.edit(checkGroup,checkItemIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }
    @RequestMapping("/deleteById")
    public Result deleteById(@Param("id") Integer id){
        try{
            checkGroupService.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }
}
