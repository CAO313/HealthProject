package com.buka.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.buka.constant.MessageConstant;
import com.buka.constant.RedisConstant;
import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.entity.Result;
import com.buka.pojo.TSetmeal;
import com.buka.service.SetMealService;
import com.buka.utils.FTPUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private SetMealService setMealService;
    @RequestMapping("/upload")
    public Result upload(MultipartFile imgFile) {
        String imgName = imgFile.getOriginalFilename();
        int index = imgName.lastIndexOf('.');
        String afterFix = imgName.substring(index - 1);
        String finalName = UUID.randomUUID().toString() + afterFix;
        try {
            FTPUtils ftpUtils = new FTPUtils();
            boolean flag = ftpUtils.uploadFile("/home/www/pages", finalName, imgFile.getInputStream());
            if (flag) {
                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,finalName);
                return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS, finalName);
            }
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    @RequestMapping("add")
    public Result add(@Param("checkGroupIds") Integer[] checkGroupIds, @RequestBody TSetmeal tSetmeal) {
        try {
            setMealService.add(checkGroupIds, tSetmeal);
            return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
    }

    @RequestMapping("findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        return setMealService.findPage(queryPageBean);
    }
}
