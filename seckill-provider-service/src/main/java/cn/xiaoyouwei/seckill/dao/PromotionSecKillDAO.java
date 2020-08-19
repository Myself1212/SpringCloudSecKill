package cn.xiaoyouwei.seckill.dao;

import cn.xiaoyouwei.seckill.entity.PromotionSecKill;

import java.util.List;

public interface PromotionSecKillDAO {
    List<PromotionSecKill> findUnstartSecKill();
}
