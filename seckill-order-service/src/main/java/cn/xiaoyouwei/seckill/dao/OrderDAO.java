package cn.xiaoyouwei.seckill.dao;

import cn.xiaoyouwei.seckill.entity.Order;

public interface OrderDAO {
    void insert(Order order);
    Order findByOrderNo(String orderNo);
}
