package cn.xiaoyouwei.seckill.service;

import cn.xiaoyouwei.seckill.client.SecKillAutoStartFeignClient;
import cn.xiaoyouwei.seckill.dao.PromotionSecKillDAO;
import cn.xiaoyouwei.seckill.entity.PromotionSecKill;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class AutoStartService {
    @Resource
    PromotionSecKillDAO promotionSecKillDAO;
//    @Resource
//    SecKillAutoStartFeignClient  secKillAutoStartFeignClient;
    @Resource
    RestTemplate restTemplate;
    public void update(PromotionSecKill ps) {
        promotionSecKillDAO.update(ps);
    }

    public List<LinkedHashMap> getSecKill(){
        return restTemplate.getForObject("http://seckill-provider-service/getSecKill",List.class);
    }

    public PromotionSecKill findById(Long psId){
        return promotionSecKillDAO.findById(psId);
    }
}
