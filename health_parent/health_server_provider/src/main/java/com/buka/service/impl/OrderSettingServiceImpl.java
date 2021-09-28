package com.buka.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.buka.dao.OrderSettingDao;
import com.buka.pojo.OrderSetting;
import com.buka.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Override
    public void add(List<OrderSetting> orderSettingList) {
        for (OrderSetting orderSetting : orderSettingList) {
            int count = orderSettingDao.countByDate(orderSetting.getOrderDate());
            if(count>0){
                orderSettingDao.update(orderSetting);
            }
            else {
                orderSettingDao.add(orderSetting);
            }
        }
    }

    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String date) {
        String begin = date+"-1";
        String end = date+"-31";
        Map<String,String> map = new HashMap();
        map.put("begin",begin);
        map.put("end",end);
       List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
       List<Map<String,Integer>> dataList = new ArrayList<>();
        for (OrderSetting orderSetting : list) {
            Map<String,Integer> m = new HashMap<>();
            m.put("date",orderSetting.getOrderDate().getDate());
            m.put("number",orderSetting.getNumber());
            m.put("reservations",orderSetting.getReservations());
            dataList.add(m);
        }
        return dataList;
    }

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        int count = orderSettingDao.countByDate(orderSetting.getOrderDate());
        if(count>0){
            orderSettingDao.update(orderSetting);
        }
        else {
            orderSettingDao.add(orderSetting);
        }
    }
}
