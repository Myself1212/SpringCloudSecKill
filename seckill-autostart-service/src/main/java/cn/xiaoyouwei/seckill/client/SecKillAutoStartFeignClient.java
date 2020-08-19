package cn.xiaoyouwei.seckill.client;

import cn.xiaoyouwei.seckill.entity.PromotionSecKill;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "SECKILL-PROVIDER-SERVICE")
public interface SecKillAutoStartFeignClient {
    @GetMapping("/getSecKill")
    List<PromotionSecKill> getSecKill();
}
