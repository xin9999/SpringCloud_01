package com.zxx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient //开启服务发现
public class OrderMain {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain.class);
        System.out.println("servers order!");
    }
}