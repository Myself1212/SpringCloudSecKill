package cn.xiaoyouwei.seckill.scheduler;

import cn.xiaoyouwei.seckill.dao.PromotionSecKillDAO;
import cn.xiaoyouwei.seckill.entity.PromotionSecKill;
import cn.xiaoyouwei.seckill.service.AutoStartService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.*;

@Component
public class SecKillAutoStartTask {
    //redisTemplate是spring封装的redis操作类，提供了一系列操作redis的模板方法
    @Resource
    RedisTemplate redisTemplate;
    @Resource
    AutoStartService autoStartService;
//    @Resource
//    RestTemplate restTemplate;

    @Scheduled(cron = "0/5 * * * * ?") //cron表达是：从零秒开始每隔5秒钟执行以下的任务调度方法
    public void autoStartSecKill() {
        //这里写成LinkedHashMap是因为这是默认类型
        List<LinkedHashMap> list = autoStartService.getSecKill();
        for (LinkedHashMap lm : list){

            Integer psIdValue  = (Integer) lm.get("psId");
            long psId = psIdValue + 0;
            PromotionSecKill ps = autoStartService.findById(psId);
            System.out.println("编号为：" + ps.getPsId() + "的秒杀活动已启动");
//            删除以前重复的活动任务
            redisTemplate.delete("seckill:count:" + ps.getPsId());
            //有几个库存商品，则初始化几个redis的List对象，表示库存
            for (int i = 0; i < ps.getPsCount(); i++) {
                redisTemplate.opsForList().rightPush("seckill:count:" + ps.getPsId(), ps.getGoodsId());
            }

            //秒杀活动开始后，需要更新状态
            ps.setStatus(1);
            System.out.println(ps);
            autoStartService.update(ps);


        }

    }
}
