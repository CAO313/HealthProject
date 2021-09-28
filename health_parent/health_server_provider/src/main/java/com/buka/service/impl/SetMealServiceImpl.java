package com.buka.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.buka.constant.RedisConstant;
import com.buka.dao.TSetmealMapper;
import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.pojo.TSetmeal;
import com.buka.service.SetMealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private TSetmealMapper tSetmealMapper;
    @Autowired
    private JedisPool jedisPool;
    @Override
    public void add(Integer[] checkGroupIds, TSetmeal tSetmeal) {
            tSetmealMapper.insertSelective(tSetmeal);
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,tSetmeal.getImg());
            int setMealID = tSetmeal.getId();
        Map<String,Integer> map = new HashMap<>();
        map.put("setMealID",setMealID);
        for (Integer checkGroupId : checkGroupIds) {
            map.put("checkGroupID",checkGroupId);
            tSetmealMapper.addCheckGroups(map);
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<TSetmeal> page = tSetmealMapper.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }
}
