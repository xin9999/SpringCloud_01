package com.zxx.controller;

import com.zxx.order.Order;
import com.zxx.properties.OrderProperties;
import com.zxx.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
//@RefreshScope
// RefreshScope用于实现 配置动态刷新 功能 —— 当配置中心
// （如 Nacos、Config Server）的配置发生变更时，被该注解标记的 Bean 会自动刷新并加载最新配置，无需重启应用。
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    OrderProperties orderProperties;
//
//    // 获取Nacos中的配置
//    @Value("${order.timeout}")
//    String orderTimeout;
//    @Value("${order.auto-confirm}")
//    String orderAutoConfirm;
//
//
//    @GetMapping("/getConfig")
//    public String getConfig() {
//        return "orderTimeout: " + orderTimeout +
//                ", orderAutoConfirm: " +orderAutoConfirm ;
//    }
    @GetMapping("/getConfig")
    public String getConfig() {
        return "orderTimeout: " + orderProperties.getTimeout() +
                ", orderAutoConfirm: " + orderProperties.getAutoConfirm() +
                ", order.db-url: " + orderProperties.getDbUrl() ;
    }

    @GetMapping("/create")
    public Order createOrder(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {
        return orderService.createOrder(productId, userId);
    }

    @GetMapping("/seckill")
    public Order seckill(@RequestParam("userId") Long userId, @RequestParam("productId") Long productId) {
        return orderService.createOrder(productId, userId);
    }

    @GetMapping("/readDB")
    public String readDB() {
        log.info("readDB......");
        return "success.....";
    }
    @GetMapping("/writeDB")
    public String wirteDB() {
        return "success.....";
    }
}
