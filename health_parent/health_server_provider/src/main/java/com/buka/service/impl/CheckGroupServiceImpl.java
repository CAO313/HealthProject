package com.buka.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.buka.dao.CheckGroupDao;
import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.pojo.CheckGroup;
import com.buka.service.CheckGroupService;
import com.buka.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl  implements CheckGroupService {

    @Autowired
    CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.add(checkGroup);
        if (checkItemIds != null && checkItemIds.length > 0) {
            Integer checkGroupId = checkGroup.getId();
            HashMap<String, Integer> map = new HashMap();
            map.put("checkGroupId", checkGroupId);
            for (Integer checkItemId : checkItemIds) {
                map.put("checkItemId", checkItemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page<CheckGroup> page = checkGroupDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    @Override
    public Integer[] getCheckItemIdsById(Integer id) {
        return checkGroupDao.getCheckItemIdsById(id);
    }

    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.updateCheckGroup(checkGroup);
        checkGroupDao.deleteRelated(checkGroup.getId());

        HashMap<String, Integer> map = new HashMap();
        map.put("checkGroupId", checkGroup.getId());
        for (Integer checkItemId : checkItemIds) {
            map.put("checkItemId", checkItemId);
            checkGroupDao.setCheckGroupAndCheckItem(map);
        }
    }

    @Override
    public void deleteById(Integer id) {
        int count = checkGroupDao.count(id);
        if(count>0){
            throw new RuntimeException();
        }
        else{
            checkGroupDao.deleteById(id);
        }
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
