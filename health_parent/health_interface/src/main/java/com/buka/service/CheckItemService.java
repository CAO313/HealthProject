package com.buka.service;

import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    void add(CheckItem checkItem);
    PageResult findPage(QueryPageBean queryPageBean);

    void deleteByid(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();
}
