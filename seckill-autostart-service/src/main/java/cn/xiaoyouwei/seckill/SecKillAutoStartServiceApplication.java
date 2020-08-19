package cn.xiaoyouwei.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@MapperScan("cn.xiaoyouwei.seckill")
//@EnableFeignClients
@EnableScheduling //启动任务调度
@EntityScan("cn.xiaoyouwei.seckill.entity")
public class SecKillAutoStartServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecKillAutoStartServiceApplication.class,args);
    }
}
