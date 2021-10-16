package com.buka.service;

import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.pojo.TSetmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {

    void add(Integer[] checkGroupIds, TSetmeal tSetmeal);

    PageResult findPage(QueryPageBean queryPageBean);

    List<TSetmeal> getAll();

    TSetmeal findByID(Integer id);

    List<Map> findSetMealCount();
}
