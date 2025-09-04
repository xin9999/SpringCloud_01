package com.zxx.feign;

import com.zxx.feign.failback.ProductFeignClientFailBack;
import com.zxx.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;


// Value 是所调用微服务的名称
@FeignClient(value = "gateway", fallback = ProductFeignClientFailBack.class)
public interface ProductFeignClient {

    /**
     * mvc注解的两套使用逻辑
     * 1、标注在Controller上，是接受这样的请求
     * 2.、标注在Feignclient上，是发送这样的请求
     */
//    @GetMapping("/api/product/product/{id}")
    @GetMapping("/api/product/product/{id}")
    Product getProductById(@PathVariable("id") Long id);
//    Product getProductById(@PathVariable("id") Long id, @RequestHeader("token") String token);
    // 接收请求时，是将路径中 {id}作为参数
    // 发送请求时，是将参数 id, 带入到路径中 {id}

}
