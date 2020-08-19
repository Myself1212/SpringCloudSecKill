package cn.xiaoyouwei.seckill.dao;

import cn.xiaoyouwei.seckill.entity.PromotionSecKill;

import java.util.List;

public interface PromotionSecKillDAO {
    //秒杀活动启动后，需要更新状态
    void update(PromotionSecKill ps);

    //根据ID查找秒杀活动
    PromotionSecKill findById(Long psId);
}
