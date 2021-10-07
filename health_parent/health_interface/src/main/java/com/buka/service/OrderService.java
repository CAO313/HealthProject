package com.buka.service;

import com.buka.entity.Result;

import java.util.Map;

public interface OrderService {
    Result order(Map map);

    Map findById(Integer id);
}
