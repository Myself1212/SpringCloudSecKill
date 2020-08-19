package cn.xiaoyouwei.seckill.service;

import cn.xiaoyouwei.seckill.dao.OrderDAO;
import cn.xiaoyouwei.seckill.entity.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OrderService {
    @Resource
    OrderDAO orderDAO;

    public Order getOrder(String orderNo){
        return orderDAO.findByOrderNo(orderNo);
    }
}
