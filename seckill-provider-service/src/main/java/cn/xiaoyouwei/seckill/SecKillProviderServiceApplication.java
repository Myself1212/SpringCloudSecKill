package cn.xiaoyouwei.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@MapperScan(value = "cn.xiaoyouwei.seckill")
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class SecKillProviderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecKillProviderServiceApplication.class,args);
    }
}
