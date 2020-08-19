package cn.xiaoyouwei.seckill.consumer;

import cn.xiaoyouwei.seckill.dao.OrderDAO;
import cn.xiaoyouwei.seckill.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Component
public class OrderConsumer {

    @Resource
    OrderDAO orderDAO;

    /**
     *
     * @param data 封装的主体数据 @Payload注解的作用：当前的map被其描述代表是主体书记
     * @param channel
     * @param headers @Header作用时动态注入头信息
     */
    @RabbitListener( //绑定消息来源
            bindings = @QueueBinding(
                    value = @Queue(value = "queue-order") ,
                    exchange = @Exchange(value = "exchange_order" , type = "topic")
            )
    )
    @RabbitHandler //声明该方法用作获取消息
    public void handleMessage(@Payload Map data , Channel channel ,
                              @Headers Map<String,Object> headers){
        System.out.println("=======获取到订单数据:" + data + "===========);");

        try {
            //在实际业务中，在创建订单时，会有对接支付宝、对接物流系统、日志登记等操作
            //为了模拟这一过程，让线程休眠一定时间
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //创建订单操作
            Order order = new Order();
            order.setOrderNo(data.get("orderNo").toString());
            order.setOrderStatus(0);
            order.setUserid(data.get("userid").toString());
            order.setRecvName("xxx");
            order.setRecvMobile("1393310xxxx");
            order.setRecvAddress("xxxxxxxxxx");
            order.setAmout(19.8f);
            order.setPostage(0f);
            order.setCreateTime(new Date());
            orderDAO.insert(order);
            Long tag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
            channel.basicAck(tag , false);//消息确认
            System.out.println(data.get("orderNo") + "订单已创建");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
