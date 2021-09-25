package com.buka.dao;

import com.buka.entity.PageResult;
import com.buka.entity.QueryPageBean;
import com.buka.pojo.CheckItem;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckItemDao {
    void add(CheckItem checkItem);

    Page<CheckItem> findPage(@Param("queryString") String queryString);

    void deleteById(Integer id);

    long countByCheckItemId(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);

    List<CheckItem> findAll();
}
