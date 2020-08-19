package cn.xiaoyouwei.seckill.service;

import cn.xiaoyouwei.seckill.dao.PromotionSecKillDAO;
import cn.xiaoyouwei.seckill.entity.PromotionSecKill;
import cn.xiaoyouwei.seckill.exception.SecKillException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class DoSecKillService {

    @Resource
    PromotionSecKillDAO promotionSecKillDAO;
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    RabbitTemplate rabbitTemplate; //Spring封装的rabbitmq的模板方法

    public void processSecKill(Long psId, String userid, Integer num) throws SecKillException {
        PromotionSecKill ps = promotionSecKillDAO.findById(psId);
        //做一些前置判断，是否符合条件参加秒杀活动
        if (ps == null) {
            //没有查询到秒杀活动，说明秒杀活动不存在
            throw new SecKillException("该商品秒杀活动不存在");
        }
        if (ps.getStatus() == 0) {

            throw new SecurityException("秒杀还未开始");

        } else if (ps.getStatus() == 2){
            throw new SecKillException("秒杀已结束，下次再见");
        }
        /**
         * 业务逻辑：
         * 1、每次从Redis商品列表中弹出一个商品，则马上增加一个userID,类型是Set
         */
        //从左弹出一个商品,返回一个商品ID，根据商品ID判断是否还有库存
        Integer goodsId = (Integer) redisTemplate.opsForList().leftPop("seckill:count:"+ps.getPsId());
        if (goodsId != null) {
            //判断该用户是不是已经抢购过了
            boolean isExisted = redisTemplate.opsForSet().isMember("seckill:users:"+ps.getPsId(),userid);
            if (!isExisted){
                //库存预减，弹出商品不为空，说明库存还有，则增加一个用户id
                System.out.println("恭喜您，抢到商品了，快去下单吧");
                redisTemplate.opsForSet().add("seckill:users:"+ps.getPsId(),userid);
            }else {
                //如果用户已经存在，则需要把上面预减的商品补上，保证商品的总数是10
                redisTemplate.opsForList().rightPush("seckill:count:"+ps.getPsId(),ps.getGoodsId());
                throw new SecKillException("抱歉，该商品每人限购1件，请勿重复下单！");
            }

        }else {
            throw new SecKillException("很遗憾，该商品已经被抢光，下次再见！");
        }

    }

    /**
     * 将抢购成功的订单发送给消息队列
     * @param userid
     */
    public String sendOrderToQueue(String userid){

        System.out.println("准备向队列发送信息");
        //用map封装基本信息
        Map data = new HashMap();
        data.put("userid",userid);
        //UUID工具类，java提供的一个工具类，可以随机生成一个全局唯一的字符串，经常用来生成订单编号等
        String orderNo = UUID.randomUUID().toString();
        data.put("orderNo",orderNo);
        //实际开发中可以再加上其他订单信息。

        //将订单信息data发送给exchange_order这个交换机
        rabbitTemplate.convertAndSend("exchange_order",null,data);

        return orderNo;
    }


}
