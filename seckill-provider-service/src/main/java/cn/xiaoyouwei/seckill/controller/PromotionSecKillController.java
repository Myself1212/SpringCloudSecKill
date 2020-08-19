package cn.xiaoyouwei.seckill.controller;

import cn.xiaoyouwei.seckill.entity.PromotionSecKill;
import cn.xiaoyouwei.seckill.service.PromotionSecKillService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class PromotionSecKillController {
    @Resource
    PromotionSecKillService promotionSecKillService;

    @GetMapping("/getSecKill")
    public List<PromotionSecKill> getSecKill(){
        return promotionSecKillService.listSecKill();
    }
}
