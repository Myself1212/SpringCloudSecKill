package cn.xiaoyouwei.seckill.service;

import cn.xiaoyouwei.seckill.dao.PromotionSecKillDAO;
import cn.xiaoyouwei.seckill.entity.PromotionSecKill;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PromotionSecKillService {
    @Resource
    PromotionSecKillDAO promotionSecKillDAO;

    public List<PromotionSecKill> listSecKill(){
        List<PromotionSecKill> list = promotionSecKillDAO.findUnstartSecKill();
        return list;
    }
}
