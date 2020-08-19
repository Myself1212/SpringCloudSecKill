package cn.xiaoyouwei.seckill.controller;

import cn.xiaoyouwei.seckill.entity.Order;
import cn.xiaoyouwei.seckill.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class OrderController {
    @Resource
    OrderService orderService;

    @GetMapping("/getOrder/{orderNo}")
    public Order list(@PathVariable("orderNo") String orderNo){
        Order order = orderService.getOrder(orderNo);
        return order;
    }
}
