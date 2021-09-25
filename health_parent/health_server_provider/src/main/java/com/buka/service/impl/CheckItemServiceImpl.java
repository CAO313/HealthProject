package com.buka.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.buka.dao.CheckItemDao;
import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.pojo.CheckItem;
import com.buka.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {

    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        Page<CheckItem> pages = checkItemDao.findPage(queryPageBean.getQueryString());
        return new PageResult(pages.getTotal(), pages.getResult());
    }

    @Override
    public void deleteByid(Integer id) {

       long count = checkItemDao.countByCheckItemId(id);
       System.out.println(count);
        if(count>0){
          throw  new RuntimeException();
        }else {
            checkItemDao.deleteById(id);
        }
    }

    @Override
    public void edit(CheckItem checkItem) {
        System.out.println(checkItem.toString());
        checkItemDao.edit(checkItem);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
       return  checkItemDao.findAll();
    }


    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
}
