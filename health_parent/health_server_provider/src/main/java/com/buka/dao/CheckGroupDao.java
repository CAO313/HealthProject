package com.buka.dao;

import com.buka.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface CheckGroupDao {
    void add(CheckGroup checkGroup);

    void setCheckGroupAndCheckItem(HashMap<String, Integer> map);

    Page<CheckGroup> findPage(@Param("queryString") String queryString);

    CheckGroup findById(Integer id);

    Integer[] getCheckItemIdsById(@Param("id") Integer id);

    void updateCheckGroup(CheckGroup checkGroup);

    void deleteRelated(Integer id);

    int count(Integer id);

    void deleteById(Integer id);

    List<CheckGroup> findAll();

}