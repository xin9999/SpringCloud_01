package com.zxx.service.impl;


import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zxx.feign.ProductFeignClient;
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

    @Autowired
    ProductFeignClient productFeignClient;

    /**
     * 被保护的资源方法
     * value: 资源名称（必填）
     * blockHandler: 处理限流/熔断等规则触发的异常
     * fallback: 处理业务异常
     * blockHandlerClass/fallbackClass: 指定异常处理方法所在的类（默认当前类）
     */
    @SentinelResource(value = "createOrder", blockHandler = "createOrderFallback")
    @Override
    public Order createOrder(Long productId, Long userId) {
//        Product product = getProductBalancedAnnotation(productId);
//        Product product = productFeignClient.getProductById(productId);

//        Product product = getProductFromRemote(productId);// 1. 普通远程调用
//        Product product = getProductBalanced(productId);  // 2. 负载均衡调用
//        Product product = getProductBalancedAnnotation(productId);  // 3. 负载均衡调用(注解式)
        Product product = productFeignClient.getProductById(productId);  // 4. 使用Feign实现远程调用

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

    // ------------------------------
    // blockHandler 方法（处理Sentinel规则异常）
    // ------------------------------
    /**
     * 注意事项：
     * 1. 方法参数、返回值需与原方法一致
     * 2. 最后一个参数必须是 BlockException（Sentinel 规则异常）
     */
    public Order createOrderFallback(Long productId, Long userId, BlockException e) {

        Order order = new Order();
        order.setId(0L);
        order.setUserId(userId);
        order.setTotalAmount(new BigDecimal(0));
        order.setNickName("未知用户");
        order.setAddress("异常信息：" + e.getClass());

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

    /**
     * 远程调用获取商品信息 - @LoadBalanced注解实现负载均衡
     */
    private Product getProductBalancedAnnotation(Long productId) {
        String url = "http://services-product/product/" + productId;
        // 发送远程请求
        log.info("发送远程请求(注解)：{}", url);
        Product product = restTemplate.getForObject(url, Product.class);

        return product;
    }
}
