package com.zxx.service.impl;


import com.zxx.order.Order;
import com.zxx.product.Product;
import com.zxx.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    LoadBalancerClient loadBalancerClient;

    @Override
    public Order createOrder(Long productId, Long userId) {
//        Product product = getProductBalancedAnnotation(productId);
//        Product product = productFeignClient.getProductById(productId);

//        Product product = getProductFromRemote(productId);// 普通远程调用
        Product product = getProductBalanced(productId);  // 负载均衡调用
        Order order = new Order();
        order.setId(1L);
        // 总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
//        order.setTotalAmount(new BigDecimal(0));
        order.setUserId(userId);
        order.setNickName("小明");
        order.setAddress("成都");
        // 商品列表
        order.setProducts(Arrays.asList(product));
        return order;
    }


    private Product getProductFromRemote(Long productId){
        //1. 获取到商品服务所在的所有机器 IP + port
        List<ServiceInstance> instances = discoveryClient.getInstances("services-product");
        ServiceInstance serviceInstance = instances.get(0);

        //远程调用URL
        String url = "http://" + serviceInstance.getHost()+":"+serviceInstance.getPort() + "/product/" + productId;
        log.info("远程请求1{}:",url);
        //2. 给远程发送请求
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }

    /**
     * 远程调用获取商品信息 - 负载均衡
     */
    private Product getProductBalanced(Long productId) {
        // 获取到商品服务所在的ip+端口
        ServiceInstance choose = loadBalancerClient.choose("services-product");
        String url = "http://" + choose.getHost() + ":" + choose.getPort() + "/product/" + productId;
        // 发送远程请求
        log.info("发送远程请求：{}", url);
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }
}
