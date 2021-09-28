package com.buka.service;


import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    PageResult findPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    Integer[] getCheckItemIdsById(Integer id);

    void edit(CheckGroup checkGroup, Integer[] checkItemIds);

    void deleteById(Integer id);

    List<CheckGroup> findAll();
}
