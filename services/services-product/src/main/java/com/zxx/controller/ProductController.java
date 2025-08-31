package com.zxx.controller;

import com.zxx.product.Product;
import com.zxx.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    //查询商品
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long productId,
                              HttpServletRequest request) {
        String xToken = request.getHeader("X-Token");
        System.out.println("test interceptors token:" + xToken);
        System.out.println("由于注解式负载均衡不打印端口号，此处测试调用了哪个机器");
        Product product =  productService.getProductById(productId);

        //测试 Sentinel 熔断策略
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return product;
    }
}
