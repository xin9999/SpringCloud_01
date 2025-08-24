package com.zxx;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableDiscoveryClient //开启服务发现
@EnableFeignClients // 开启Feign远程调用功能
public class OrderMain {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain.class);
        System.out.println("servers order!");
    }

    /**
     * 使用nacosConfigManager监听配置的变化：
     * 	1.项目启动，监听配置文件
     * 	2.项目发生变化后拿到值
     * 	3.发送邮件
     * @param nacosConfigManager
     * @return
     */
    @Bean
    ApplicationRunner applicationRunner(NacosConfigManager nacosConfigManager) {
        return args -> {
            ConfigService configService = nacosConfigManager.getConfigService();
            configService.addListener("service-order.properties", "DEFAULT_GROUP", new Listener() {
                @Override
                public Executor getExecutor() {
                    return Executors.newFixedThreadPool(4);
                }

                @Override
                public void receiveConfigInfo(String s) {
                    System.out.println("变化的配置：" + s);
                    System.out.println("邮件通知配置变化");
                }
            });
            System.out.println("============nacosConfigManager监听配置============");
        };
    }

}