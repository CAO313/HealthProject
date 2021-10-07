package com.buka.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.buka.constant.MessageConstant;
import com.buka.dao.MemberDao;
import com.buka.dao.OrderDao;
import com.buka.dao.OrderSettingDao;
import com.buka.entity.Result;
import com.buka.pojo.Member;
import com.buka.pojo.Order;
import com.buka.pojo.OrderSetting;
import com.buka.service.OrderService;
import com.buka.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Override
    public Result order(Map map) {
        Integer setMealID = Integer.parseInt((String)map.get("setmealId"));
        Date orderDate = null;
        try {
            orderDate = DateUtils.parseString2Date( (String) map.get("orderDate"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //检查该日期是否设置预约
       int count = orderSettingDao.countByDate(orderDate);
        if(count==0){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //检查是否约满
        int number = orderSettingDao.getNumberByDate(orderDate);
        int reversions = orderSettingDao.getReversionsByDate(orderDate);
        if(reversions>=number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //检查是否重复预约
        String telephone = (String) map.get("telephone");
       Member member = memberDao.findByTelephone(telephone);
        //检查是否是会员
        if(member==null){
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber((String) map.get("telephone"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
           Order order = new Order(member.getId(),orderDate,setMealID);
           List list = orderDao.findByCondition(order);
           if(list!=null&&list.size()>0){
               return new Result(false,MessageConstant.HAS_ORDERED);
           }
           //预约成功
            order.setOrderStatus(Order.ORDERSTATUS_NO);
           order.setOrderType(Order.ORDERTYPE_WEIXIN);
            orderDao.add(order);
            //更新ordersetting
            OrderSetting  orderSetting = orderSettingDao.getOrderSettingByDate(orderDate);
            orderSetting.setReservations(orderSetting.getReservations()+1);
            orderSettingDao.update(orderSetting);
            return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id){
        Map map = orderDao.findById4Detail(id);
        return map;
    }
}
