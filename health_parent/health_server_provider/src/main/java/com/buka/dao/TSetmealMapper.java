package com.buka.dao;

import com.buka.pojo.TSetmeal;
import com.buka.pojo.TSetmealExample;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TSetmealMapper {
    long countByExample(TSetmealExample example);

    int deleteByExample(TSetmealExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(TSetmeal record);

    int insertSelective(TSetmeal record);

    List<TSetmeal> selectByExample(TSetmealExample example);

    TSetmeal selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") TSetmeal record, @Param("example") TSetmealExample example);

    int updateByExample(@Param("record") TSetmeal record, @Param("example") TSetmealExample example);

    int updateByPrimaryKeySelective(TSetmeal record);

    int updateByPrimaryKey(TSetmeal record);

    void addCheckGroups(Map<String,Integer> map);

    Page<TSetmeal> findPage(@Param("queryString") String queryString);
}