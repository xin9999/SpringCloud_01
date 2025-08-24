package com.zxx.config;

import feign.Logger;
import feign.RetryableException;
import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OrderServiceConfig {

    @LoadBalanced  // 注解式负载均衡
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

//    添加配置组件
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }


    // 添加超时配置
//    @Bean
//    Retryer retryer(){
//
//        return new Retryer.Default();   //  new 的是Default 内部类
//    }
}
