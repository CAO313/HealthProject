package com.buka.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.buka.constant.RedisConstant;
import com.buka.dao.CheckGroupDao;
import com.buka.dao.CheckItemDao;
import com.buka.dao.TSetmealMapper;
import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.pojo.CheckGroup;
import com.buka.pojo.CheckItem;
import com.buka.pojo.TSetmeal;
import com.buka.pojo.TSetmealExample;
import com.buka.service.SetMealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private TSetmealMapper tSetmealMapper;
    @Autowired
    private JedisPool jedisPool;
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Autowired
    private CheckItemDao checkItemDao;
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

    @Override
    public List<TSetmeal> getAll() {
        TSetmealExample tSetmealExample = new TSetmealExample();
        List<TSetmeal> list =  tSetmealMapper.selectByExample(tSetmealExample);
        return list;
    }

    @Override
    public TSetmeal findByID(Integer id) {
        TSetmeal tSetmeal = tSetmealMapper.selectByPrimaryKey(id);
        List<CheckGroup> checkGroups = new ArrayList<>();

       Integer[] checkGroupIDS  = tSetmealMapper.findCheckGoupsByID(id);
        for (Integer checkGroupID : checkGroupIDS) {
            List<CheckItem> checkItems = new ArrayList<>();
          CheckGroup checkGroup = checkGroupDao.findById(checkGroupID);
          Integer[] checkItemIDs = checkGroupDao.getCheckItemIdsById(checkGroupID);
            for (Integer itemID : checkItemIDs) {
                CheckItem checkItem = checkItemDao.findById(itemID);
                checkItems.add(checkItem);
            }
            checkGroup.setCheckItems(checkItems);
            checkGroups.add(checkGroup);
            System.out.println(checkGroup);
        }
        tSetmeal.setCheckGroups(checkGroups);
        System.out.println(tSetmeal);
        return tSetmeal;
    }
}
