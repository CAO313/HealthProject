package com.buka.service;

import com.buka.pojo.Order;
import com.buka.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {
     void add(List<OrderSetting> orderSettingList);

     List<Map<String, Integer>> getOrderSettingByMonth(String date);

     void editNumberByDate(OrderSetting orderSetting);
}
