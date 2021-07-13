package cn.szxy.staservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.szxy"})  //指定扫描位置
@EnableDiscoveryClient //启动nacos注册  客户端
@EnableFeignClients // 开启nacos远程调用
@EnableScheduling //开启定时器任务注解
//@MapperScan("cn.szxy.userservice.mapper")
public class StaApplication {

    public static void main(String[] args) {
        SpringApplication.run(StaApplication.class,args);
    }
}
