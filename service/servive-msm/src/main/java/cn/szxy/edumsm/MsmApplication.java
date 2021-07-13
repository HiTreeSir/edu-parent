package cn.szxy.edumsm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"cn.szxy"})  //指定扫描位置
@EnableDiscoveryClient //启动nacos注册  客户端
public class MsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsmApplication.class,args);
    }
}
